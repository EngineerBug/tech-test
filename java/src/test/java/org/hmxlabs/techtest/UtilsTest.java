package org.hmxlabs.techtest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    private final String PLAIN_TEXT = Constant.DUMMY_DATA;
    private final String PLAIN_TEXT_2 = "DIFFERENT_PLAIN_TEXT";
    private final String HASH = "cecfd3953783df706878aaec2c22aa70";

    @Test
    public void testHashString() {
        assertThat(Utils.hashUTF8ToMD5(PLAIN_TEXT)).isEqualTo(HASH);
    }

    @Test
    public void testDifferentStringsGetDifferentHashes() {
        assertThat(Utils.hashUTF8ToMD5(PLAIN_TEXT_2)).isNotEqualTo(HASH);
    }

    @Test
    public void testSameStringGetsSameHash() {
        String hash = Utils.hashUTF8ToMD5(PLAIN_TEXT);
        assertThat(Utils.hashUTF8ToMD5(PLAIN_TEXT)).isEqualTo(hash);
    }
}
