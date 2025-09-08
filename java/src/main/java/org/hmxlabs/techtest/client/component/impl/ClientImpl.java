package org.hmxlabs.techtest.client.component.impl;

import org.hmxlabs.techtest.client.api.model.DataEnvelope;
import org.hmxlabs.techtest.client.component.Client;
import org.hmxlabs.techtest.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

/**
 * Client code does not require any test coverage
 * Contains code for sending and reciving requests to/from the server.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    private final WebClient webClient;

    /**
     * A method that sends an HTTP request to the server to persist a data.
     * Note: may fail due to new hadoop service instability.
     * 
     * @param dataEnvelope - the data being persisted
     */
    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        log.debug("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), Constant.URI_PUSHDATA);

        try {
            Boolean response = webClient
                .post()
                .uri(Constant.URI_PUSHDATA)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dataEnvelope)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            if (response.booleanValue()) {
                log.info("Successfully pushed data {} to {}", dataEnvelope.getDataHeader().getName(), Constant.URI_PUSHDATA);
            } else {
                log.warn("Failed to push data {} to {}", dataEnvelope.getDataHeader().getName(), Constant.URI_PUSHDATA);
            }

        } catch (Exception e) {
            log.error("Exception: failed to push data {} to {}", dataEnvelope.getDataHeader().getName(), Constant.URI_PUSHDATA);
        }
    }

    /**
     * Sends a get request to the server's URI_GETDATA endpoint
     * retrieves all datablocks of a particular type.
     * 
     * @param blockType - the type of blocks to fetch
     * @returns List<DataEnvelope> - all the data encoded as DataEnvelope objects.
     */
    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        try {
            List<DataEnvelope> dataEnvelopes = webClient
                .get()
                .uri(Constant.URI_GETDATA, blockType)
                .retrieve()
                .bodyToFlux(DataEnvelope.class)
                .collectList()
                .block();

            if (dataEnvelopes.isEmpty()) {
                log.warn("No datas of type {} were found at {}.", blockType, Constant.URI_GETDATA);
            } else {
                log.info("Successfully found datas of type {}", blockType, Constant.URI_GETDATA);
            }
            return dataEnvelopes;

        } catch (Exception e) {
            log.error("Exception: failed to get datas of type {} from {}", blockType, Constant.URI_GETDATA);
            return Collections.emptyList();
        }
    }

    /**
     * Sends a request to the server's URI_PATCHDATA endpoint 
     * to update the type of a block with a particular name.
     * 
     * @param blockName - the unique identifying name of the block
     * @param newBlockType - the type the block should be updated TO.
     * @return boolean - has the server updated the field?
     */
    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        try {
            Boolean response = webClient
                .patch()
                .uri(Constant.URI_PATCHDATA, blockName, newBlockType)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            if (response) {
                log.info("Successfully updated block {} to type {}", blockName, newBlockType);
            } else {
                log.warn("Failed to update block {} to type ", blockName, newBlockType);
            }
            return response;
        } catch (Exception e) {
            log.error("Exception: failed to update block {} to type {}", blockName, newBlockType);
            return false;
        }
    }
}
