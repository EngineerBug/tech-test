package org.hmxlabs.techtest.server.persistence.model;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.TEST_NAME;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;

@ExtendWith(MockitoExtension.class)
public class DataBodyEntityTests {

    @Test
    public void assignDataBodyEntityFieldsShouldWorkAsExpected() {
        Instant expectedTimestamp = Instant.now();

        DataHeaderEntity dataHeaderEntity = new DataHeaderEntity();
        dataHeaderEntity.setName(TEST_NAME);
        dataHeaderEntity.setBlocktype(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity.setCreatedTimestamp(expectedTimestamp);

        DataBodyEntity dataBodyEntity = createTestDataBodyEntity(dataHeaderEntity);

        assertThat(dataBodyEntity.getDataHeaderEntity()).isNotNull();
        assertThat(dataBodyEntity.getDataBody()).isNotNull();
    }

    @Test
    public void checkTwoDataBodiesAreEqualAsExpected() {

        DataHeaderEntity dataHeaderEntity1 = new DataHeaderEntity();
        dataHeaderEntity1.setName(TEST_NAME);
        dataHeaderEntity1.setBlocktype(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity1.setCreatedTimestamp(Instant.now());
        DataBodyEntity dataBodyEntity1 = createTestDataBodyEntity(dataHeaderEntity1);

        DataHeaderEntity dataHeaderEntity2 = new DataHeaderEntity();
        dataHeaderEntity2.setName(TEST_NAME);
        dataHeaderEntity2.setBlocktype(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity2.setCreatedTimestamp(Instant.now().plusSeconds(100L));
        DataBodyEntity dataBodyEntity2 = createTestDataBodyEntity(dataHeaderEntity2);

        assertThat(dataBodyEntity1).isEqualTo(dataBodyEntity2);
    }
}
