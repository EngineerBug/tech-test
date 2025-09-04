package org.hmxlabs.techtest.server.persistence.model;

import org.hmxlabs.techtest.TestDataHelper;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DataHeaderEntityTests {


    @Test
    public void assignDataHeaderEntityFieldsShouldWorkAsExpected() {
        Instant expectedTimestamp = Instant.now();

        DataHeaderEntity dataHeaderEntity = TestDataHelper.createTestDataHeaderEntity(expectedTimestamp);

        assertThat(dataHeaderEntity.getName()).isEqualTo(TestDataHelper.TEST_NAME);
        assertThat(dataHeaderEntity.getBlocktype()).isEqualTo(BlockTypeEnum.BLOCKTYPEA);
        assertThat(dataHeaderEntity.getCreatedTimestamp()).isEqualTo(expectedTimestamp);
    }


}
