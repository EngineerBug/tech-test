package org.hmxlabs.techtest.server.api.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hmxlabs.techtest.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hmxlabs.techtest.TestDataHelper;
import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;

/**
 * Source tutorial: https://spring.io/guides/gs/testing-web
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class ServerControllerComponentE2E {

    @LocalServerPort
	private int port;

    @Autowired
    private ServerController serverController;

    private ObjectMapper objectMapper;

	private MockMvc mockMvc;

    private DataEnvelope dataEnvelope;

    private final String ADDRESS = String.format("http://localhost:%d/", port);
	public final String URI_ISOK = ADDRESS + "dataserver/isok";
    public final String URI_PUSHDATA = ADDRESS + "dataserver/pushdata";
    public final String URI_GETDATA = ADDRESS + "dataserver/data/{blockType}";
    public final String URI_PATCHDATA = ADDRESS + "dataserver/update/{name}/{newBlockType}";

    @BeforeEach
	public void setUp() throws HadoopClientException, NoSuchAlgorithmException, IOException {
		mockMvc = standaloneSetup(serverController).build();
		objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.build();

		dataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();
	}

    @Test
    public void testSanity() {
        assertThat(mockMvc).isNotNull();
        assertThat(serverController).isNotNull();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    public void testIsOK() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get(URI_ISOK))
            .andExpect(status().isOk())
            .andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();
    }

    @Test
    public void testCreate_success() throws Exception {
        String testDataEnvelopeJson = objectMapper.writeValueAsString(dataEnvelope);

		MvcResult postResult = mockMvc.perform(post(URI_PUSHDATA)
                    .content(testDataEnvelopeJson)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(postResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();

        MvcResult checkResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEA))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(checkResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isNotEmpty();
		assertThat(dataEnvelopes.get(0)).isEqualTo(dataEnvelope);
    }

    @Test
    public void testUpdate_success() throws Exception {
        // Given
        String testDataEnvelopeJson = objectMapper.writeValueAsString(dataEnvelope);

		MvcResult postResult = mockMvc.perform(post(URI_PUSHDATA)
				    .content(testDataEnvelopeJson)
				    .contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		boolean postPassed = Boolean.parseBoolean(postResult.getResponse().getContentAsString());
		assertThat(postPassed).isTrue();

        // When
        MvcResult patchResult = mockMvc.perform(patch(URI_PATCHDATA, dataEnvelope.getDataHeader().getName(), BlockTypeEnum.BLOCKTYPEB))
				.andExpect(status().isOk())
				.andReturn();

        boolean patchPassed = Boolean.parseBoolean(patchResult.getResponse().getContentAsString());
		assertThat(patchPassed).isTrue();

        // Then
        MvcResult checkResult = mockMvc.perform(get(Constant.URI_GETDATA, BlockTypeEnum.BLOCKTYPEB))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes = objectMapper.readValue(checkResult.getResponse().getContentAsString(), new TypeReference<List<DataEnvelope>>() {});

		assertThat(dataEnvelopes).isNotNull();
    	assertThat(dataEnvelopes).isNotEmpty();
		assertThat(dataEnvelopes.get(0)).isEqualTo(TestDataHelper.createTestDataEnvelopeWithTypeB());
    }
}
