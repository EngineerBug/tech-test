package org.hmxlabs.techtest.server.service.impl;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.persistence.repository.DataHeaderRepository;
import org.springframework.stereotype.Service;

/**
 * Calls the DataHeaderRespository class to interact with the database.
 */
@Service
@RequiredArgsConstructor
public class DataHeaderServiceImpl implements org.hmxlabs.techtest.server.service.DataHeaderService {

    private final DataHeaderRepository dataHeaderRepository;

    @Override
    public void saveHeader(DataHeaderEntity entity) {
        dataHeaderRepository.save(entity);
    }

    @Override
    public Optional<DataHeaderEntity> getDataHeaderByName(String name) {
        return dataHeaderRepository.findByName(name);
    }
}
