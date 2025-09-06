package org.hmxlabs.techtest.server.component;

import org.hmxlabs.techtest.server.api.model.DataEnvelope;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;

import java.io.IOException;
import java.util.List;

public interface Server {
    boolean saveDataEnvelope(DataEnvelope envelope) throws IOException;
    List<DataEnvelope> getDataEnvelopesOfType(BlockTypeEnum blockType) throws IOException;
    boolean updateBlockType(String name, BlockTypeEnum newBlockType) throws IOException;
}
