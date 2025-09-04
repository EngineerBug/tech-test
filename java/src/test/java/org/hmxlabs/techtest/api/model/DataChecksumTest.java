package org.hmxlabs.techtest.api.model;

import org.hmxlabs.techtest.server.api.model.DataChecksum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DataChecksumTest {

    public static final String DUMMY_DATA = "cecfd3953783df706878aaec2c22aa70";

    @Test
    public void assignDataBodyFieldsShouldWorkAsExpected() {
        DataChecksum dataChecksum = new DataChecksum(DUMMY_DATA);

        assertThat(dataChecksum).isNotNull();
        assertThat(dataChecksum.getDataChecksum()).isEqualTo(DUMMY_DATA);
    }
}