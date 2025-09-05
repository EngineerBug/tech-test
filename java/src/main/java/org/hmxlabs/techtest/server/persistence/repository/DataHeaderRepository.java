package org.hmxlabs.techtest.server.persistence.repository;

import java.util.Optional;

import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataHeaderRepository extends JpaRepository<DataHeaderEntity, Long> {
    Optional<DataHeaderEntity> findByName(String blockName);
}
