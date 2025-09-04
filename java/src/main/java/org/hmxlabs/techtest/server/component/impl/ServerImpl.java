package org.hmxlabs.techtest.server.component.impl;

import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.service.DataBodyService;
import org.hmxlabs.techtest.server.component.Server;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    public List<DataEnvelope> getDataEnvelopesOfType(BlockTypeEnum blockType) throws IOException {
        return Collections.emptyList();
    }
}
