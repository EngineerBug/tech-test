package org.hmxlabs.techtest.server.component.impl;

import org.hmxlabs.techtest.server.api.model.DataBody;
import org.hmxlabs.techtest.server.api.model.DataChecksum;
import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.api.model.DataHeader;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.service.DataBodyService;
import org.hmxlabs.techtest.server.service.DataHeaderService;
import org.hmxlabs.techtest.server.service.HadoopService;
import org.hmxlabs.techtest.server.component.Server;
import org.hmxlabs.techtest.server.exception.HadoopClientException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hmxlabs.techtest.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;

    private final DataHeaderService dataHeaderServiceImpl;

    private final ModelMapper modelMapper;

    private final HadoopService hadoopService;

    /**
     * Exercise 2, 5
     * 
     * 
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) {

        String serverChecksumString = Utils.hashUTF8ToMD5(envelope.getDataBody().getDataBody());

        if (Objects.equals(serverChecksumString, envelope.getDataChecksum().getDataChecksum())) {
            persist(envelope);
            log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());

            // Start async datalake persistence

            return true;
        }
        log.warn("Data NOT persisted due to mismatched checksums, data name: {}", envelope.getDataHeader().getName());
        log.warn("Checksums: client, {} and server, {}", envelope.getDataChecksum(), serverChecksumString);
        return false;
    }

    /**
     * Exercise 2, 5
     * Converts the DataEnvelope to a DataBodyEntity that the database can persist.
     * Then calls the method to send the data to the database.
     * @param envelope
     */
    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);
        dataBodyEntity.setDataCheckSum(envelope.getDataChecksum().getDataChecksum());

        saveDataToDatabase(dataBodyEntity);
        try {
            saveDataToLake(dataBodyEntity);
        } catch (HadoopClientException e) {
            log.error("The external hadoop service failed");
        }
    }

    /**
     * Exercise 2
     * Makes the actual call to the service layer to persist the data.
     * @param dataBodyEntity
     */
    private void saveDataToDatabase(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

    /**
     * Exercise 5
     * @param dataBodyEntity
     * @throws HadoopClientException
     */
    private void saveDataToLake(DataBodyEntity dataBodyEntity) throws HadoopClientException {
        hadoopService.saveBlockToHadoop(dataBodyEntity);
    }

    /**
     * Exercise 3
     * Gets all the datas from the database with a particular type.
     * @param blockType - the type of block to get
     * @returns a list of DataEnvolopes, all with the same type
     */
    @Override
    public List<DataEnvelope> getDataEnvelopesOfType(BlockTypeEnum blockType) {
        List<DataBodyEntity> dataBodyEntities = dataBodyServiceImpl.getDataByBlockType(blockType);
        log.info("Retrieved list of {} DataEnvelopes of type {}", dataBodyEntities.size(), blockType);
        return mapDataBodyEntitiesToDataEnvelopes(dataBodyEntities);
    }

    /**
     * Exercise 3
     * 
     * Maps a list of DataBodyEntity objects to a list of DataEnvelope objects.
     * @param dataBodyEntities
     * @return dataEnvelopes
     */
    private List<DataEnvelope> mapDataBodyEntitiesToDataEnvelopes(List<DataBodyEntity> dataBodyEntities) {
        return dataBodyEntities.stream()
            .map(data -> mapDataBodyEntityToDataEnvelope(data))
            .toList();
    }

    /**
     * Exercise 3
     * 
     * @param dataBodyEntity
     * @return
     */
    private DataEnvelope mapDataBodyEntityToDataEnvelope(DataBodyEntity dataBodyEntity) {
        return new DataEnvelope(
            new DataHeader(dataBodyEntity.getDataHeaderEntity().getName(), dataBodyEntity.getDataHeaderEntity().getBlocktype()),
            new DataBody(dataBodyEntity.getDataBody()), 
            new DataChecksum(dataBodyEntity.getDataCheckSum()));
    }

    /**
     * Exercise 4
     * 
     */
    @Override
    public boolean updateBlockType(String name, BlockTypeEnum newBlockType) {
        Optional<DataHeaderEntity> dataHeaderOptional = dataHeaderServiceImpl.getDataHeaderByName(name);
        if (dataHeaderOptional.isPresent()) {
            dataHeaderOptional.get().setBlocktype(newBlockType);
            dataHeaderServiceImpl.saveHeader(dataHeaderOptional.get());
            return checkUpdatePersisted(name, newBlockType);
        } else {
            return false;
        }
    }
    
    /**
     * Exercise 4
     * 
     */
    private boolean checkUpdatePersisted(String name, BlockTypeEnum newBlockType) {
        Optional<DataHeaderEntity> dataHeaderOptional = dataHeaderServiceImpl.getDataHeaderByName(name);
        if (dataHeaderOptional.isPresent()) {
            if (dataHeaderOptional.get().getBlocktype().equals(newBlockType)) {
                return true;
            }
        }
        return false;
    }
}
