package org.hmxlabs.techtest.client.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@JsonSerialize(as = DataEnvelope.class)
@JsonDeserialize(as = DataEnvelope.class)
@Getter
@AllArgsConstructor
public class DataEnvelope {

    @NotNull
    private DataHeader dataHeader;

    @NotNull
    private DataBody dataBody;

    @NotNull
    private DataChecksum dataChecksum;
}
