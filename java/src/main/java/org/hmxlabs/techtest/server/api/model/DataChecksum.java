package org.hmxlabs.techtest.server.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonSerialize(as = DataChecksum.class)
@JsonDeserialize(as = DataChecksum.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DataChecksum {
    
    @NotNull
    private String dataChecksum;
}
