package com.netease.nim.demo.utils;

public class HexDump {

    private static final char HEX_CODE_CHARS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final int SHIFT_TABLE[] = {60, 56, 52, 48, 44, 40, 36, 32, 28,
            24, 20, 16, 12, 8, 4, 0};



    private static String toHex(final long value, final int digitNum) {
        StringBuilder result = new StringBuilder(digitNum);

        for (int j = 0; j < digitNum; j++) {
            int index = (int) ((value >> SHIFT_TABLE[j + (16 - digitNum)]) & 15);
            result.append(HEX_CODE_CHARS[index]);
        }

        return result.toString();
    }

    public static String toHex(final byte value) {
        return toHex(value, 2);
    }

    public static String toHex(final short value) {
        return toHex(value, 4);
    }

    public static String toHex(final int value) {
        return toHex(value, 8);
    }

    public static String toHex(final long value) {
        return toHex(value, 16);
    }

    public static String toHex(final byte[] value) {
        return toHex(value, 0, value.length);
    }

    public static String toHex(final byte[] value, final int offset,
                               final int length) {
        StringBuilder retVal = new StringBuilder();

        int end = offset + length;
        for (int x = offset; x < end; x++)
            retVal.append(toHex(value[x]));

        return retVal.toString();
    }

    public static byte[] restoreBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; ++i) {
            int c1 = charToNumber(hex.charAt(2 * i));
            int c2 = charToNumber(hex.charAt(2 * i + 1));
            if (c1 == -1 || c2 == -1) {
                return null;
            }
            bytes[i] = (byte) ((c1 << 4) + c2);
        }

        return bytes;
    }

    private static int charToNumber(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 0xa;
        } else if (c >= 'A' && c <= 'F') {
            return c - 'A' + 0xA;
        } else {
            return -1;
        }
    }
}
