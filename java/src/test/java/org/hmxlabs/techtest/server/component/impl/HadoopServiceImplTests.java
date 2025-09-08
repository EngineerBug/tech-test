package org.hmxlabs.techtest.server.component.impl;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.hmxlabs.techtest.Constant;
import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.service.impl.HadoopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class HadoopServiceImplTests {
    
    @Mock
    private WebClient webClientMock;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private HadoopServiceImpl hadoopServiceImpl;

    private DataBodyEntity expectedDataBodyEntity;

    @BeforeEach
    public void setup() {
        expectedDataBodyEntity = createTestDataBodyEntity(createTestDataHeaderEntity(Instant.now()));
    }
 
    @Test
    public void testNoExceptionThrownOnSuccess() throws Exception {

        when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Constant.URI_PUSHBIGDATA)).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(expectedDataBodyEntity)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HttpStatus.class)).thenReturn(Mono.just(HttpStatus.OK));

        assertThatNoException().isThrownBy(() -> {
            hadoopServiceImpl.saveBlockToHadoop(expectedDataBodyEntity);
        });
    }

    @Test
    public void testExceptionThrownOnFailure() throws Exception {

        when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Constant.URI_PUSHBIGDATA)).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(expectedDataBodyEntity)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HttpStatus.class))
            .thenReturn(Mono.error(new WebClientResponseException(
                "504 Gateway Timeout",
                HttpStatus.GATEWAY_TIMEOUT.value(),
                HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(),
                null,
                null,
                null
            )));

        assertThatThrownBy(() -> {
            hadoopServiceImpl.saveBlockToHadoop(expectedDataBodyEntity);
        })
        .isInstanceOf(HadoopClientException.class)
        .hasMessage("Hadoop service encountered an error.");
        
    }
}
