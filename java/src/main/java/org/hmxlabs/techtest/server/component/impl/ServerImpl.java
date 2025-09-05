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
import org.hmxlabs.techtest.server.component.Server;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hmxlabs.techtest.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final DataHeaderService dataHeaderServiceImpl;
    private final ModelMapper modelMapper;

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) {

        String serverChecksumString = Utils.hashUTF8ToMD5(envelope.getDataBody().getDataBody());

        if (Objects.equals(serverChecksumString, envelope.getDataChecksum().getDataChecksum())) {
            persist(envelope);
            log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
            return true;
        }
        log.warn("Data NOT persisted due to mismatched checksums, data name: {}", envelope.getDataHeader().getName());
        log.warn("Checksums: client, {} and server, {}", envelope.getDataChecksum(), serverChecksumString);
        return false;
    }

    /**
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

        saveData(dataBodyEntity);
    }

    /**
     * Makes the actual call to the service layer to persist the data.
     * @param dataBodyEntity
     */
    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

    /**
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
     * Maps a list of DataBodyEntity objects to a list of DataEnvelope objects.
     * @param dataBodyEntities
     * @return dataEnvelopes
     */
    private List<DataEnvelope> mapDataBodyEntitiesToDataEnvelopes(List<DataBodyEntity> dataBodyEntities) {
        return dataBodyEntities.stream()
            .map(data -> mapDataBodyEntityToDataEnvelope(data))
            .toList();
    }

    private DataEnvelope mapDataBodyEntityToDataEnvelope(DataBodyEntity dataBodyEntity) {
        return new DataEnvelope(
            new DataHeader(dataBodyEntity.getDataHeaderEntity().getName(), dataBodyEntity.getDataHeaderEntity().getBlocktype()),
            new DataBody(dataBodyEntity.getDataBody()), 
            new DataChecksum(dataBodyEntity.getDataCheckSum()));
    }

    @Override
    public boolean updateBlockType(String name, BlockTypeEnum newBlockType) throws IOException {
        Optional<DataHeaderEntity> dataHeaderOptional = dataHeaderServiceImpl.getDataHeaderByName(name);
        return verifyHeader(dataHeaderOptional, newBlockType);
    }

    private boolean verifyHeader(Optional<DataHeaderEntity> dataHeaderOptional, BlockTypeEnum newBlockType) {
        if (dataHeaderOptional.isEmpty()) {
            return false;
        }
        updateAndSaveHeader(dataHeaderOptional.get(), newBlockType);
        return true;
    }

    private void updateAndSaveHeader(DataHeaderEntity dataHeaderEntity, BlockTypeEnum newBlockType) {
        dataHeaderEntity.setBlocktype(newBlockType);
        dataHeaderServiceImpl.saveHeader(dataHeaderEntity);
    }
}
