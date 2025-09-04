package org.hmxlabs.techtest.server.persistence.model;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.TEST_NAME;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;

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

        DataHeaderEntity dataHeaderEntity1 = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity1 = createTestDataBodyEntity(dataHeaderEntity1);

        DataHeaderEntity dataHeaderEntity2 = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity2 = createTestDataBodyEntity(dataHeaderEntity2);

        assertThat(dataBodyEntity1).isEqualTo(dataBodyEntity2);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithHeaderNames() {

        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        differentHeader.setName("NOT_TEST_NAME");
        DataBodyEntity dataBodyEntity1 = createTestDataBodyEntity(differentHeader);

        DataHeaderEntity dataHeaderEntity2 = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity2 = createTestDataBodyEntity(dataHeaderEntity2);

        assertThat(dataBodyEntity1).isNotEqualTo(dataBodyEntity2);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithBodies() {

        DataHeaderEntity header = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity1 = createTestDataBodyEntity(header);
        dataBodyEntity1.setDataBody("SOME_DIFFERENT_DATA");

        DataBodyEntity dataBodyEntity2 = createTestDataBodyEntity(header);

        assertThat(dataBodyEntity1).isNotEqualTo(dataBodyEntity2);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithHeaderTypes() {

        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        differentHeader.setBlocktype(BlockTypeEnum.BLOCKTYPEB);
        DataBodyEntity dataBodyEntity1 = createTestDataBodyEntity(differentHeader);

        DataHeaderEntity dataHeaderEntity2 = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity2 = createTestDataBodyEntity(dataHeaderEntity2);

        assertThat(dataBodyEntity1).isNotEqualTo(dataBodyEntity2);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithWithDifferentClass() {

        DataHeaderEntity dataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity dataBodyEntity = createTestDataBodyEntity(dataHeaderEntity);

        assertThat(dataBodyEntity).isNotEqualTo(new String("hello world"));
    }
}
