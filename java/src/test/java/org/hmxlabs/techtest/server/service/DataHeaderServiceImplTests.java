package org.hmxlabs.techtest.server.service;

import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.persistence.repository.DataHeaderRepository;
import org.hmxlabs.techtest.server.service.impl.DataHeaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataHeaderServiceImplTests {

    @Mock
    private DataHeaderRepository dataHeaderRepositoryMock;

    @InjectMocks
    private DataHeaderServiceImpl dataHeaderServiceImpl;

    private DataHeaderEntity expectedDataHeaderEntity;

    @BeforeEach
    public void setup() {
        expectedDataHeaderEntity = createTestDataHeaderEntity(Instant.now());
    }

    @Test
    public void shouldSaveDataHeaderEntityAsExpected(){
        dataHeaderServiceImpl.saveHeader(expectedDataHeaderEntity);

        verify(dataHeaderRepositoryMock, times(1))
                .save(eq(expectedDataHeaderEntity));
    }
}