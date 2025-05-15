package com.mezzala.common;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static String generate(String category) {
        SecureRandom random = new SecureRandom();
        String result = "";

        if (category.equals("key")) {
            byte[] key = new byte[32]; // 256 bit
            random.nextBytes(key);
            System.out.println("Secret Key (Base64): " + Base64.getEncoder().encodeToString(key));
            result = Base64.getEncoder().encodeToString(key);
        }

        if (category.equals("iv")) {
            byte[] iv = new byte[16]; // 128 bit
            random.nextBytes(iv);
            System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));
            result = Base64.getEncoder().encodeToString(iv);
        }

        return result;
    }
}
