package org.hmxlabs.techtest.server.api.controller;

import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.component.Server;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;

    @GetMapping(value = "/isok", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isServerOk() throws IOException, NoSuchAlgorithmException {
        log.info("Someone checked if the server is ok");
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws IOException, NoSuchAlgorithmException {

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

        log.info("Data envelope persisted. Attribute name: {}", dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }

}
