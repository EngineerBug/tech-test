package org.hmxlabs.techtest.server.service;

import org.hmxlabs.techtest.server.exception.HadoopClientException;
import org.hmxlabs.techtest.server.persistence.model.DataBodyEntity;

public interface HadoopService {
    void saveBlockToHadoop(DataBodyEntity dataBodyEntity) throws HadoopClientException;
}
