package org.hmxlabs.techtest;

import java.nio.charset.StandardCharsets;

import org.springframework.util.DigestUtils;

/**
 * Hashes a UTF-8 string using the MD5 hash algorithm.
 * 
 * @param data - the string to be hashed
 * @returns String - the hashed string
 */
public class Utils {
    public static String hashUTF8ToMD5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }
}
