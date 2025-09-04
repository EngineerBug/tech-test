package org.hmxlabs.techtest.server.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonSerialize(as = DataEnvelope.class)
@JsonDeserialize(as = DataEnvelope.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DataEnvelope {
    
    @NotNull
    @Valid
    private DataHeader dataHeader;

    @NotNull
    private DataBody dataBody;

    @NotNull
    private DataChecksum dataChecksum;
}
