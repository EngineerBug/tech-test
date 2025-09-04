package org.hmxlabs.techtest;

import java.nio.charset.StandardCharsets;

import org.springframework.util.DigestUtils;

public class Utils {
    public static String hashUTF8ToMD5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }
}
