package com.github.sbooster.templates.backend.rsocket.security.crypt;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ByteCryptUtils {
    public static String encrypt(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        List<Integer> list = new ArrayList<>();
        for (byte value : bytes) {
            int halfPow = (int) Math.pow(value, 2) * 2;
            list.add(halfPow);

        }
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("|"));
    }

    public static String decrypt(String data) {
        double[] doubles = Arrays.stream(data.split("\\|"))
                .mapToDouble(Double::valueOf)
                .toArray();
        int length = doubles.length;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) Math.sqrt(doubles[i] / 2);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
