package org.hmxlabs.techtest;

import org.hmxlabs.techtest.server.api.model.DataBody;
import org.hmxlabs.techtest.server.api.model.DataChecksum;
import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.api.model.DataHeader;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;
import org.hmxlabs.techtest.server.persistence.model.DataHeaderEntity;

import java.time.Instant;

public class TestDataHelper {

    public static final String TEST_NAME = "Test";
    public static final String TEST_NAME_EMPTY = "";
    public static final String DUMMY_DATA = "AKCp5fU4WNWKBVvhXsbNhqk33tawri9iJUkA5o4A6YqpwvAoYjajVw8xdEw6r9796h1wEp29D";
    public static final DataChecksum CHECKSUM = new DataChecksum(Utils.hashUTF8ToMD5(DUMMY_DATA));

    public static DataHeaderEntity createTestDataHeaderEntity(Instant expectedTimestamp) {
        DataHeaderEntity dataHeaderEntity = new DataHeaderEntity();
        dataHeaderEntity.setName(TEST_NAME);
        dataHeaderEntity.setBlocktype(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity.setCreatedTimestamp(expectedTimestamp);
        return dataHeaderEntity;
    }

    public static DataBodyEntity createTestDataBodyEntity(DataHeaderEntity dataHeaderEntity) {
        DataBodyEntity dataBodyEntity = new DataBodyEntity();
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);
        dataBodyEntity.setDataBody(DUMMY_DATA);
        return dataBodyEntity;
    }

    public static DataEnvelope createTestDataEnvelopeApiObject() {
        DataBody dataBody = new DataBody(DUMMY_DATA);
        DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody, CHECKSUM);
        return dataEnvelope;
    }

    public static DataEnvelope createFaultyTestDataEnvelopeApiObject() {
        DataBody dataBody = new DataBody(DUMMY_DATA);
        DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);
        DataChecksum dataChecksum = new DataChecksum(Utils.hashUTF8ToMD5("SOME INCORRECT DATA"));

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody, dataChecksum);
        return dataEnvelope;
    }

    public static DataEnvelope createTestDataEnvelopeApiObjectWithEmptyName() {
        DataBody dataBody = new DataBody(DUMMY_DATA);
        DataHeader dataHeader = new DataHeader(TEST_NAME_EMPTY, BlockTypeEnum.BLOCKTYPEA);

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody, CHECKSUM);
        return dataEnvelope;
    }
}
