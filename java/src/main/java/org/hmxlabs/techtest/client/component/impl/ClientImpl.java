package org.hmxlabs.techtest.client.component.impl;

import org.hmxlabs.techtest.client.api.model.DataEnvelope;
import org.hmxlabs.techtest.client.component.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriTemplate;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * Client code does not require any test coverage
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    private final WebClient webClient;

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        log.debug("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);

        try {
            Boolean response = webClient
                .post()
                .uri(URI_PUSHDATA)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dataEnvelope)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            if (response.booleanValue()) {
                log.info("Successfully pushed data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
            } else {
                log.warn("Failed to push data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
            }

        } catch (Exception e) {
            log.error("Exception: failed to push data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
            throw e;
        }
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        return null;
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        return true;
    }
}
