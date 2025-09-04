package org.hmxlabs.techtest.client.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(as = DataChecksum.class)
@JsonDeserialize(as = DataChecksum.class)
@Getter
@AllArgsConstructor
public class DataChecksum {

    @NotNull
    private String dataChecksum;

}