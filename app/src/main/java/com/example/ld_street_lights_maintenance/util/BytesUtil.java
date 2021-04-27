package com.example.ld_street_lights_maintenance.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BytesUtil {

    public static <T> byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    /**
     * int 转化为byte[]
     *
     * @param id
     * @return
     */
    public static byte[] int2Bytes(int id) {

        /*
         * byte[] arr = new byte[4];
         * arr[0] = (byte) ((id >> 0 * 8) & 0xff);
         * arr[1] = (byte) ((id >> 1 * 8) & 0xff);
         * arr[2] = (byte) ((id >> 2 *8) & 0xff);
         * arr[3] = (byte) ((id >> 3 * 8) & 0xff);
         */

        byte[] arr = new byte[4];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) ((id >> i * 8) & 0xff);
        }
        return arr;
    }

    /**
     * byte[] 转化为 int
     *
     * @return
     */
    public static int bytes2Int(byte[] arr) {

		/*int rs0 = (int) ((arr[0] & 0xff) << 0 * 8);
        int rs1 = (int) ((arr[1] & 0xff) << 1 * 8);
		int rs2 = (int) ((arr[2] & 0xff) << 2 * 8);
		int rs3 = (int) ((arr[3] & 0xff) << 3 * 8);*/


        int rs = 0;
        for (int i = 0; i < arr.length; i++) {
            rs += (int) ((arr[i] & 0xff) << i * 8);
        }

        return rs;
    }


    //高位在前，低位在后
    public static byte[] intBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte) ((num >>> 24) & 0xff);//说明一
        result[1] = (byte) ((num >>> 16) & 0xff);
        result[2] = (byte) ((num >>> 8) & 0xff);
        result[3] = (byte) ((num >>> 0) & 0xff);
        return result;
    }

    //高位在前，低位在后
    public static int bytesInt(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }


    //高位在前，低位在后，区分byte长度
    public static int bytesIntHL(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }else if(bytes.length == 2){
            int a = (bytes[0] & 0xff) <<  8;
            int b = (bytes[1] & 0xff);
            result = a | b;
        }
        return result;
    }

    //高位在前，低位在后，区分byte长度
    public static byte[] intBytesHL(int num, int length) {

        byte[] result = new byte[length];
        if (length == 2) {
            result[0] = (byte) ((num >>> 8) & 0xff);
            result[1] = (byte) ((num >>> 0) & 0xff);
        } else if (length == 4) {
            result[0] = (byte) ((num >>> 24) & 0xff);//说明一
            result[1] = (byte) ((num >>> 16) & 0xff);
            result[2] = (byte) ((num >>> 8) & 0xff);
            result[3] = (byte) ((num >>> 0) & 0xff);
        }

        return result;
    }



    /**
     *判断字符串是否为乱码
     * @param strName
     * @return
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (char c : ch) {
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        return result > 0.4;

    }
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }


    /**
     *  判断String中是否包含乱码
     * @param strName
     * @return
     */
    public static boolean isMessyCode2(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String bytesToHex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=(byte)Integer.parseInt((inHex.substring(i,i+2)),16);
            j++;
        }
        return result;
    }


    /**
     * byte大数组按大小拆分小数组
     * @param ary 要分割的数组
     * @param subSize 分割的块大小
     * @return
     */
    public static Object[] splitAry(byte[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;

        List<List<Byte>> subAryList = new ArrayList<List<Byte>>();

        for (int i = 0; i < count; i++) {
            int index = i * subSize;
            List<Byte> list = new ArrayList<Byte>();
            int j = 0;
            while (j < subSize && index < ary.length) {
                list.add(ary[index++]);
                j++;
            }
            subAryList.add(list);
        }

        Object[] subAry = new Object[subAryList.size()];

        for(int i = 0; i < subAryList.size(); i++){
            List<Byte> subList = subAryList.get(i);
            byte[] subAryItem = new byte[subList.size()];
            for(int j = 0; j < subList.size(); j++){
                subAryItem[j] = subList.get(j);
            }
            subAry[i] = subAryItem;
        }

        return subAry;
    }

    /**
     * 合并多个byte
     * @param values
     * @return
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }


    /**
     * Bit转Byte
     */
    public static byte BitToByte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {//4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    /**
     * Byte转Bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

}
