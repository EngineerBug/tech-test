package org.hmxlabs.techtest.client.component.impl;

import org.hmxlabs.techtest.client.api.model.DataEnvelope;
import org.hmxlabs.techtest.client.component.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Collections;
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
    public static final String URI_GETDATA = "http://localhost:8090/dataserver/data/{blockType}";
    public static final String URI_PATCHDATA = "http://localhost:8090/dataserver/update/{name}/{newBlockType}";

    /**
     * A method that sends an HTTP request to the server to persist a data
     * @param dataEnvelope - the data being persisted
     */
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
        }
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        try {
            List<DataEnvelope> dataEnvelopes = webClient
                .get()
                .uri(URI_GETDATA, blockType)
                .retrieve()
                .bodyToFlux(DataEnvelope.class)
                .collectList()
                .block();

            if (dataEnvelopes.isEmpty()) {
                log.warn("No datas of type {} were found at {}.", blockType, URI_GETDATA);
            } else {
                log.info("Successfully found datas of type {}", blockType, URI_GETDATA);
            }
            return dataEnvelopes;

        } catch (Exception e) {
            log.error("Exception: failed to get datas of type {} from {}", blockType, URI_GETDATA);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        return true;
    }
}
