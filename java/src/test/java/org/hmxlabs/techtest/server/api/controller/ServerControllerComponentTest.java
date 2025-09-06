package org.hmxlabs.techtest.server.api.controller;

import org.hmxlabs.techtest.TestDataHelper;
import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.component.Server;
import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.Constant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class ServerControllerComponentTest {

	@Mock
	private Server serverMock;

	private DataEnvelope testDataEnvelope;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	private ServerController serverController;

	@BeforeEach
	public void setUp() throws HadoopClientException, NoSuchAlgorithmException, IOException {
		serverController = new ServerController(serverMock);
		mockMvc = standaloneSetup(serverController).build();
		objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.build();

		testDataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();
	}

	@Test
	public void testIsServerOk() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(Constant.URI_ISOK))
            .andExpect(status().isOk())
            .andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();
	}

	@Test
	public void testPushDataPostCall_worksAsExpected() throws Exception {
		when(serverMock.saveDataEnvelope(any(DataEnvelope.class))).thenReturn(true);
		String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

		MvcResult mvcResult = mockMvc.perform(post(Constant.URI_PUSHDATA)
				.content(testDataEnvelopeJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();
	}

	@Test
	public void testPushDataPostCall_failsOnChecksumDifference() throws Exception {
		when(serverMock.saveDataEnvelope(any(DataEnvelope.class))).thenReturn(false);
		String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

		MvcResult mvcResult = mockMvc.perform(post(Constant.URI_PUSHDATA)
				.content(testDataEnvelopeJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(400))
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isFalse();
	}

	@Test
	public void testPushDataPostCall_throwsInternalServerError() throws Exception {
		when(serverMock.saveDataEnvelope(any(DataEnvelope.class))).thenThrow(new IOException());
		String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

		MvcResult mvcResult = mockMvc.perform(post(Constant.URI_PUSHDATA)
				.content(testDataEnvelopeJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(500))
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isFalse();
	}

	@Test
	public void testGetAllDatasByType_successEmpty() throws Exception {
		when(serverMock.getDataEnvelopesOfType(any(BlockTypeEnum.class))).thenReturn(Collections.emptyList());

		MvcResult mvcResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEA))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isEmpty();
	}

	@Test
	public void testGetAllDatasByType_successSingleton() throws Exception {
		when(serverMock.getDataEnvelopesOfType(any(BlockTypeEnum.class))).thenReturn(Collections.singletonList(testDataEnvelope));

		MvcResult mvcResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEA))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isNotEmpty();
		assertThat(dataEnvelopes.get(0)).isEqualTo(testDataEnvelope);
	}

	@Test
	public void testGetAllDatasByType_successMultiple() throws Exception {
		when(serverMock.getDataEnvelopesOfType(any(BlockTypeEnum.class))).thenReturn(Collections.nCopies(5, testDataEnvelope));

		MvcResult mvcResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEA))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).hasSize(5);
		assertThat(dataEnvelopes.get(0)).isEqualTo(testDataEnvelope);
	}

	@Test
	public void testGetAllDatasByType_invalidBlockType() throws Exception {
		mockMvc.perform(get(Constant.URI_GETDATA, "INVALID"))
				.andExpect(status().is(400))
				.andExpect(content().string(""));
	}

	@Test
	public void testGetAllDatasByType_throwsInternalServerError() throws Exception {
		when(serverMock.getDataEnvelopesOfType(any(BlockTypeEnum.class))).thenThrow(new IOException());

		MvcResult mvcResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEA))
				.andExpect(status().is(500))
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});
		assertThat(dataEnvelopes).isNotNull();
		assertThat(dataEnvelopes).isEmpty();
	}

	@Test
	public void testUpdateBlockType_success() throws Exception {
		when(serverMock.updateBlockType(any(String.class), any(BlockTypeEnum.class))).thenReturn(true);

		MvcResult mvcResult = mockMvc.perform(patch(Constant.URI_PATCHDATA, "NAME", BlockTypeEnum.BLOCKTYPEB))
				.andExpect(status().isOk())
				.andReturn();

		boolean didUpdate = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(didUpdate).isTrue();
	}

	@Test
	public void testUpdateBlockType_nonExistantBlock() throws Exception {
		when(serverMock.updateBlockType(any(String.class), any(BlockTypeEnum.class))).thenReturn(false);

		MvcResult mvcResult = mockMvc.perform(patch(Constant.URI_PATCHDATA, "NAME", BlockTypeEnum.BLOCKTYPEB))
				.andExpect(status().is(404))
				.andReturn();

		boolean didUpdate = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(didUpdate).isFalse();
	}
	
	@Test
	public void testUpdateBlockType_invalidBlockType() throws Exception {
		MvcResult mvcResult = mockMvc.perform(patch(Constant.URI_PATCHDATA, "NAME", "INVALID_BLOCK_TYPE"))
				.andExpect(status().is(400))
				.andReturn();

		boolean didUpdate = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(didUpdate).isFalse();
	}

	@Test
	public void testUpdateBlockType_throwsInternalServerError() throws Exception {
		when(serverMock.updateBlockType(any(String.class), any(BlockTypeEnum.class))).thenThrow(new IOException());

		MvcResult mvcResult = mockMvc.perform(patch(Constant.URI_PATCHDATA, "NAME", BlockTypeEnum.BLOCKTYPEB))
				.andExpect(status().is(500))
				.andReturn();

		boolean didUpdate = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(didUpdate).isFalse();
	}
}
