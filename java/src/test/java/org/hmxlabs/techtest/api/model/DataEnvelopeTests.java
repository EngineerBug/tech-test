package org.hmxlabs.techtest.api.model;

import org.hmxlabs.techtest.Utils;
import org.hmxlabs.techtest.server.api.model.DataChecksum;
import org.hmxlabs.techtest.server.api.model.DataBody;
import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.api.model.DataHeader;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hmxlabs.techtest.TestDataHelper.DUMMY_DATA;
import static org.hmxlabs.techtest.TestDataHelper.TEST_NAME;

@ExtendWith(MockitoExtension.class)
public class DataEnvelopeTests {

    @Test
    public void assignDataHeaderFieldsShouldWorkAsExpected() {
        DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);
        DataBody dataBody = new DataBody(DUMMY_DATA);
        DataChecksum dataChecksum = new DataChecksum(Utils.hashUTF8ToMD5(dataBody.getDataBody()));

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody, dataChecksum);

        assertThat(dataEnvelope).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isNotNull();
        assertThat(dataEnvelope.getDataBody()).isNotNull();
        assertThat(dataEnvelope.getDataChecksum()).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isEqualTo(dataHeader);
        assertThat(dataEnvelope.getDataHeader()).isEqualTo(dataHeader);
        assertThat(dataEnvelope.getDataChecksum()).isEqualTo(dataChecksum);
        assertThat(dataBody.getDataBody()).isEqualTo(DUMMY_DATA);
    }
}
