package org.hmxlabs.techtest.server.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Transactional
// @Rollback
public class ServerControllerComponentE2E {
    
    // @Autowired
    // private TestRestTemplate restTemplate;

    // @Test
    // public void testIsOK() {
    //     ResponseEntity<Boolean> response = restTemplate.getForEntity("/data/isok", Boolean.class);

    //     assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    //     assertThat(response.getBody()).isTrue();
    // }
}
