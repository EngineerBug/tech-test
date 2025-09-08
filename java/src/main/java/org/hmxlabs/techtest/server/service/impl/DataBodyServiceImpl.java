package org.hmxlabs.techtest.server.service.impl;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.repository.DataStoreRepository;
import org.hmxlabs.techtest.server.service.DataBodyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Calls the DataBodyRespository classes to interact with the database.
 */
@Service
@RequiredArgsConstructor
public class DataBodyServiceImpl implements DataBodyService {

    private final DataStoreRepository dataStoreRepository;

    @Override
    public void saveDataBody(DataBodyEntity dataBody) {
        dataStoreRepository.save(dataBody);
    }

    @Override
    public List<DataBodyEntity> getDataByBlockType(BlockTypeEnum blockType) {
        return dataStoreRepository.findByDataHeaderEntity_Blocktype(blockType);
    }

    @Override
    public Optional<DataBodyEntity> getDataByBlockName(String blockName) {
        return dataStoreRepository.findByDataHeaderEntity_Name(blockName);
    }
}
