package org.hmxlabs.techtest.server.service;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.persistence.repository.DataStoreRepository;
import org.hmxlabs.techtest.server.service.impl.DataBodyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataBodyServiceTests {

    public static final String TEST_NAME_NO_RESULT = "TestNoResult";

    @Mock
    private DataStoreRepository dataStoreRepositoryMock;

    private DataBodyService dataBodyService;

    private DataBodyEntity expectedDataBodyEntity;

    @BeforeEach
    public void setup() {
        DataHeaderEntity testDataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        expectedDataBodyEntity = createTestDataBodyEntity(testDataHeaderEntity);

        dataBodyService = new DataBodyServiceImpl(dataStoreRepositoryMock);
    }

    @Test
    public void testSaveDataBody_success() {
        dataBodyService.saveDataBody(expectedDataBodyEntity);

        verify(dataStoreRepositoryMock, times(1))
                .save(eq(expectedDataBodyEntity));
    }

    @Test
    public void testGetDataByBlockType_success() {
        dataBodyService.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA);

        verify(dataStoreRepositoryMock, times(1))
                .findByDataHeaderEntity_Blocktype(eq(BlockTypeEnum.BLOCKTYPEA));
    }

    @Test
    public void testGetDataByBlockName_success() {
        when(dataStoreRepositoryMock.findByDataHeaderEntity_Name(anyString())).thenReturn(Optional.of(expectedDataBodyEntity));

        String name = expectedDataBodyEntity.getDataHeaderEntity().getName();
        Optional<DataBodyEntity> dataBodyEntity = dataBodyService.getDataByBlockName(name);

        assertThat(dataBodyEntity.get()).isEqualTo(expectedDataBodyEntity);
        verify(dataStoreRepositoryMock, times(1)).findByDataHeaderEntity_Name(eq(name));
    }

    @Test
    public void testGetDataByBlockName_nonExistantName() {
        when(dataStoreRepositoryMock.findByDataHeaderEntity_Name(anyString())).thenReturn(Optional.empty());

        String name = expectedDataBodyEntity.getDataHeaderEntity().getName();
        Optional<DataBodyEntity> dataBodyEntity = dataBodyService.getDataByBlockName(name);

        assertThat(dataBodyEntity).isEmpty();
        verify(dataStoreRepositoryMock, times(1)).findByDataHeaderEntity_Name(eq(name));
    }
}
