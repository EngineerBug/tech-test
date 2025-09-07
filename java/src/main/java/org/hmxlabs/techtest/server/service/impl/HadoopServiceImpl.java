package org.hmxlabs.techtest.server.service.impl;

import org.hmxlabs.techtest.Constant;
import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.service.HadoopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HadoopServiceImpl implements HadoopService {

    private final WebClient webClient;

    @Override
    public void saveBlockToHadoop(DataBodyEntity dataBodyEntity) throws HadoopClientException {
        String blockName = dataBodyEntity.getDataHeaderEntity().getName();
        log.info("Uploading block {} to hadoop...", blockName);
        
        HttpStatus response = webClient
            .post()
            .uri(Constant.URI_PUSHBIGDATA)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dataBodyEntity)
            .retrieve()
            .bodyToMono(HttpStatus.class)
            .block();

        if (response.is2xxSuccessful()) {
            log.info("Successfully uploaded block {} to hadoop", blockName);
        } else if (response.value() == HttpStatus.GATEWAY_TIMEOUT.value()) {
            log.error("Did not persist block {} to hadoop due to {} error", blockName, response.value());
            throw new HadoopClientException("timeout");
        }
    }
}
