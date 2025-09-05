package org.hmxlabs.techtest.server.component.impl;

import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.component.Server;
import org.hmxlabs.techtest.server.mapper.ServerMapperConfiguration;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;
import org.hmxlabs.techtest.server.service.DataBodyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hmxlabs.techtest.TestDataHelper.createFaultyTestDataEnvelopeApiObject;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTests {

    @Mock
    private DataBodyService dataBodyServiceImplMock;

    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    private Server server;

    @BeforeEach
    public void setup() {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));
        expectedDataBodyEntity.setDataCheckSum(testDataEnvelope.getDataChecksum().getDataChecksum());

        server = new ServerImpl(dataBodyServiceImplMock, modelMapper);
    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException {
        boolean success = server.saveDataEnvelope(testDataEnvelope);

        assertThat(success).isTrue();
        verify(dataBodyServiceImplMock, times(1)).saveDataBody(eq(expectedDataBodyEntity));
    }

    @Test
    public void shouldNotSaveDataWhenChecksumsDoNotMatch() throws NoSuchAlgorithmException, IOException {
        DataEnvelope faultyDataEnvelope = createFaultyTestDataEnvelopeApiObject();
        boolean success = server.saveDataEnvelope(faultyDataEnvelope);

        assertThat(success).isFalse();
    }

    @Test
	public void testGetDataEnvelopesOfType_successEmpty() throws Exception {
		when(dataBodyServiceImplMock.getDataByBlockType(any(BlockTypeEnum.class))).thenReturn(Collections.emptyList());

        List<DataEnvelope> dataEnvelopes = server.getDataEnvelopesOfType(BlockTypeEnum.BLOCKTYPEA);

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isEmpty();
	}

	@Test
	public void testGetDataEnvelopesOfType_successSingleton() throws Exception {
		when(dataBodyServiceImplMock.getDataByBlockType(any(BlockTypeEnum.class))).thenReturn(Collections.singletonList(expectedDataBodyEntity));

        List<DataEnvelope> dataEnvelopes = server.getDataEnvelopesOfType(BlockTypeEnum.BLOCKTYPEA);

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isNotEmpty();
		assertThat(dataEnvelopes.get(0)).isEqualTo(testDataEnvelope);
	}

	@Test
	public void testGetDataEnvelopesOfType_successMultiple() throws Exception {
		when(dataBodyServiceImplMock.getDataByBlockType(any(BlockTypeEnum.class))).thenReturn(Collections.nCopies(5, expectedDataBodyEntity));

		List<DataEnvelope> dataEnvelopes = server.getDataEnvelopesOfType(BlockTypeEnum.BLOCKTYPEA);

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).hasSize(5);
		assertThat(dataEnvelopes.get(0)).isEqualTo(testDataEnvelope);
	}
}
