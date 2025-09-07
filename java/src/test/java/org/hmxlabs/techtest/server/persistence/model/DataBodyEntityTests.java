package org.hmxlabs.techtest.server.persistence.model;

import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.TEST_NAME;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.hmxlabs.techtest.TestDataHelper.createTestDataHeaderEntity;

public class DataBodyEntityTests {

    DataBodyEntity dataBodyEntity;

    DataHeaderEntity dataHeaderEntity;

    @BeforeEach
    public void setup() {
        dataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        dataBodyEntity = createTestDataBodyEntity(dataHeaderEntity);
    }

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
        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity differentBody = createTestDataBodyEntity(differentHeader);

        assertThat(differentBody).isEqualTo(dataBodyEntity);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithHeaderNames() {
        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        differentHeader.setName("NOT_TEST_NAME");
        DataBodyEntity differentBody = createTestDataBodyEntity(differentHeader);

        assertThat(differentBody).isNotEqualTo(dataBodyEntity);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithBodies() {
        DataBodyEntity differentBody = createTestDataBodyEntity(dataHeaderEntity);
        differentBody.setDataBody("SOME_DIFFERENT_DATA");

        assertThat(differentBody).isNotEqualTo(dataBodyEntity);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqualWithHeaderTypes() {
        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        differentHeader.setBlocktype(BlockTypeEnum.BLOCKTYPEB);
        DataBodyEntity differentBody = createTestDataBodyEntity(differentHeader);

        assertThat(differentBody).isNotEqualTo(dataBodyEntity);
    }

    @Test
    public void checkTwoDataBodiesAreNotEqual_differentClass() {
        assertThat(dataBodyEntity).isNotEqualTo(new String("hello world"));
    }

    @Test
    public void checkTwoDataBodiesAreNotEqual_differentChecksum() {
        DataHeaderEntity differentHeader = createTestDataHeaderEntity(Instant.now());
        DataBodyEntity differentBody = createTestDataBodyEntity(differentHeader);
        differentBody.setDataCheckSum("DIFFERENT_CHECKSUM");

        assertThat(differentBody).isNotEqualTo(dataBodyEntity);
    }
}
