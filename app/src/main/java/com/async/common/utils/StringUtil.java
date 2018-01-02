package com.async.common.utils;

/**
 * author: Ling Lin
 * created on: 2017/8/2 20:49
 * email: 10525677@qq.com
 * description:
 */

public class StringUtil {

    public static String byteToHexString(byte[] src, int start, int length) {
        if (src == null || src.length < length + start || length <= 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int index = start; index < start + length; index++) {
            byte data = src[index];
            String hv = Integer.toHexString(data);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String byteToHex(byte data){
        StringBuilder stringBuilder = new StringBuilder();
        String hv = Integer.toHexString(data);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        return stringBuilder.append(hv).toString().toUpperCase();
    }

    public static int hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int result = 0;
        for (int i = 0; i < hexString.length(); i++) {
            int value = hexToByte(hexString.substring(i, i + 1));
            result = result * 16 + value;
        }
        return result;
    }

    private static int hexToByte(String hex) {
        return "0123456789ABCDEF".indexOf(hex);
    }

    /*0x80: -127
    * 0xFF: -1
    * 0x7F: 127
    * */
    public static int hexToInteger(String hexString) {
        int result = Integer.parseInt(hexString, 16);
        if (result > 127) {
            result -= 256;
        }
        return result;
    }
}
