package org.hmxlabs.techtest.server.service;

import java.util.Optional;

import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;

public interface DataHeaderService {
    void saveHeader(DataHeaderEntity entity);
    Optional<DataHeaderEntity> getDataHeaderByName(String name);
}
