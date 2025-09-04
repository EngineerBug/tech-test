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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/**
 * This class contains all the API endpoints for the server.
 * @url /dataserver
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
     * @return Boolean - true
     * @throws IOException
     */
    @GetMapping(value = "/isok", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isServerOk() throws IOException {
        log.info("Someone checked if the server is ok");
        return ResponseEntity.ok(true);
    }

    /**
     * Endpoint for adding a piece of data in the database.
     * Will fail if the calculated chekcsum was different to the submitted checksum.
     * 
     * @param dataEnvelope
     * @return Boolean - whether the object was successfully persisted
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws IOException, NoSuchAlgorithmException {
        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

        if (checksumPass) {
            return ResponseEntity.ok(checksumPass);
        }
        return new ResponseEntity<Boolean>(false, HttpStatusCode.valueOf(400));
    }

    /**
     * 
     * @param blocktype - 
     * @return 
     * @throws IOException
     */
    @GetMapping(value = "/blocktype/{blockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataEnvelope>> getByBlockType(@PathVariable BlockTypeEnum blockType) throws IOException {

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}
