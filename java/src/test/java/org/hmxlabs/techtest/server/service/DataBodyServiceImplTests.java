package org.hmxlabs.techtest.server.service;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.persistence.repository.DataStoreRepository;
import org.hmxlabs.techtest.server.service.impl.DataBodyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataBodyServiceImplTests {

    public static final String TEST_NAME_NO_RESULT = "TestNoResult";

    @Mock
    private DataStoreRepository dataStoreRepositoryMock;

    @InjectMocks
    private DataBodyServiceImpl dataBodyServiceImpl;

    private DataBodyEntity expectedDataBodyEntity;

    private DataHeaderEntity expectedDataHeaderEntity;

    @BeforeEach
    public void setup() {
        expectedDataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        expectedDataBodyEntity = createTestDataBodyEntity(expectedDataHeaderEntity);
    }

    @Test
    public void testSaveDataBody_success() {
        dataBodyServiceImpl.saveDataBody(expectedDataBodyEntity);

        verify(dataStoreRepositoryMock, times(1))
                .save(eq(expectedDataBodyEntity));
    }

    @Test
    public void testGetDataByBlockType_success() {
        dataBodyServiceImpl.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA);

        verify(dataStoreRepositoryMock, times(1))
                .findByDataHeaderEntity_Blocktype(eq(BlockTypeEnum.BLOCKTYPEA));
    }
}
