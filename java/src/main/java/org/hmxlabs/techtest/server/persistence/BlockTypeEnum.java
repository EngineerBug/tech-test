package org.hmxlabs.techtest.server.persistence;

/**
 * Represents what types of blocks can be stored in the database.
 */
public enum BlockTypeEnum {
    BLOCKTYPEA("blocktypea"),
    BLOCKTYPEB("blocktypeb");

    private final String type;

    BlockTypeEnum(String type) {
        this.type = type;
    }

}
