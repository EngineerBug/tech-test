package org.hmxlabs.techtest.server.api.model;

import java.util.Objects;

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

    /**
     * Two DataEnvelopes are said to be equal if:
     * - names are the same
     * - of the same type
     * - contains the same data
     * - calculates to the same checksum
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DataEnvelope that = (DataEnvelope) obj;

        return Objects.equals(this.dataHeader.getName(), that.dataHeader.getName()) &&
           Objects.equals(this.dataHeader.getBlockType(), that.dataHeader.getBlockType()) &&
           Objects.equals(this.dataBody.getDataBody(), that.getDataBody().getDataBody()) &&
           Objects.equals(this.dataChecksum.getDataChecksum(), that.getDataChecksum().getDataChecksum());
    } 
}
