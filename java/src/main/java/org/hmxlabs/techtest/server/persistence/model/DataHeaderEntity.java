package org.hmxlabs.techtest.server.persistence.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hmxlabs.techtest.server.persistence.BlockTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "DATA_HEADER",
        uniqueConstraints = @UniqueConstraint(columnNames="NAME")
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataHeaderEntity {

    @Id
    @SequenceGenerator(name = "dataHeaderSequenceGenerator", sequenceName = "SEQ_DATA_HEADER", allocationSize = 1)
    @GeneratedValue(generator = "dataHeaderSequenceGenerator")
    @Column(name = "DATA_HEADER_ID")
    private Long dataHeaderId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BLOCKTYPE")
    @Enumerated(EnumType.STRING)
    private BlockTypeEnum blocktype;

    @Column(name = "CREATED_TIMESTAMP")
    private Instant createdTimestamp;

    @PrePersist
    public void setTimestamps() {
        if (createdTimestamp == null) {
            createdTimestamp = Instant.now();
        }
    }
}
