package org.hmxlabs.techtest.server.api.controller;

import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.component.Server;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class contains all the API endpoints for the server.
 * The @xxxMapping annotations denote methods directly exposed to the network for clients to call.
 */
@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;

    /**
     * A simple endpoint that will always return true to confirm the server is working as expected.
     * 
     * @return Boolean - true
     */
    @GetMapping(value = "/isok", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isServerOk() {
        log.info("Someone checked if the server is ok");
        return ResponseEntity.ok(true);
    }

    /**
     * Endpoint for adding a piece of data in the database.
     * Will fail if the calculated chekcsum was different to the submitted checksum.
     * 
     * @param dataEnvelope - the data to be persisted in a new data block
     * @return ResponseEntity<Boolean> - whether the object was successfully persisted; wrapped for network communication
     */
    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) {
        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        try {
            boolean checksumPass = server.saveDataEnvelope(dataEnvelope);
            
            if (checksumPass) {
                return ResponseEntity.ok(checksumPass);
            }
            return new ResponseEntity<Boolean>(false, HttpStatusCode.valueOf(400));

        } catch (IOException e) {
            return new ResponseEntity<Boolean>(false, HttpStatusCode.valueOf(500));
        }
    }

    /**
     * Endpoint for fetching pieces of data in the database of the same type.
     * Spring automatically handles not being able to resolve blockType to a BlockTypeEnum
     * 
     * @param blocktype - 
     * @return List<DataEnvelope> - all data blocks with the required type; wrapped for network communication
     */
    @GetMapping(value = "/data/{blockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataEnvelope>> getByBlockType(@PathVariable("blockType") BlockTypeEnum blockType) {
        log.info("Getting all data envelopes of type: {}", blockType);
        try {
            List<DataEnvelope> dataEnvelopes = server.getDataEnvelopesOfType(blockType);
            return ResponseEntity.ok(dataEnvelopes);

        } catch (IOException e) {
            return new ResponseEntity<List<DataEnvelope>>(Collections.emptyList(), HttpStatusCode.valueOf(500));
        }
    }

    /**
     * Endpoint for updating the type of a piece of data in the database.
     * 
     * @param name - the unique identifier of a block of data
     * @param newBlockType - the block type the data should be updated to.
     * @return ResponseEntity<Boolean> - if the block was successfully updated; wrapped for network communication
     */
    @PatchMapping(value = "/update/{name}/{newBlockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateBlockType(@PathVariable("name") String name, @PathVariable("newBlockType") BlockTypeEnum newBlockType) {
        log.info("Updating type of datablock {} to: {}", name, newBlockType);
        try {
            boolean isUpdated = server.updateBlockType(name, newBlockType);

            if (isUpdated) {
                log.info("Successfully updated block {} to blocktype {}.", name, newBlockType);
                return ResponseEntity.ok(true);
            }

            log.warn("Failed to updated block {} to blocktype {}.", name, newBlockType);
            return new ResponseEntity<Boolean>(false, HttpStatusCode.valueOf(400));

        } catch (IOException e) {
            return new ResponseEntity<Boolean>(false, HttpStatusCode.valueOf(500));
        }
    }
}
