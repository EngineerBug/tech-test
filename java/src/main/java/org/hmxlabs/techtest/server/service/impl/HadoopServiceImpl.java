package org.hmxlabs.techtest.server.service.impl;

import org.hmxlabs.techtest.Constant;
import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.service.HadoopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HadoopServiceImpl implements HadoopService {

    private final WebClient webClient;

    @Override
    @Retryable
    public void saveBlockToHadoop(DataBodyEntity dataBodyEntity) throws HadoopClientException {
        String blockName = dataBodyEntity.getDataHeaderEntity().getName();
        log.info("Uploading block {} to hadoop...", blockName);
        
        try {
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
            } else {
                log.warn("Failed to upload block {} to hadoop due to {} response", blockName, response.value());
                throw new HadoopClientException("Persist failed");
            }
        } catch (WebClientResponseException e) {
            log.error("Failed to upload block {} to hadoop due to exception: {}", blockName, e.getMessage());
            throw new HadoopClientException("Hadoop service encountered an error.");
        }
    }
}
