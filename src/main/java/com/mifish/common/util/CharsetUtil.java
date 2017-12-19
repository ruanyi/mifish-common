package com.mifish.common.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * CharsetUtil
 *
 * @author ruanlsh
 * @time:2013-07-02
 */
public final class CharsetUtil {

    private CharsetUtil() {
    }

    private static Map<String, Charset> charsets = new HashMap<String, Charset>();

    static {
        charsets.put("UTF-8", Charset.forName("UTF-8"));
        charsets.put("GBK", Charset.forName("GBK"));
        charsets.put("GB2312", Charset.forName("GB2312"));
        charsets.put("ISO_8859_1", Charset.forName("ISO-8859-1"));
    }

    public static void registerCharset(String charset) {
        if (!isExist(charset)) {
            String csStr = charset.toUpperCase();
            synchronized (charsets) {
                if (!isExist(csStr)) {
                    charsets.put(csStr, Charset.forName(csStr));
                }
            }
        }
    }

    public static boolean isExist(String charset) {
        return charsets.containsKey(charset);
    }

    /***getSysCharset*/
    public static String getSysCharset() {
        return System.getProperty("file.encoding");
    }

    public static byte[] toBytes(String str, String charset) {
        return toBytes(str.toCharArray(), charset);
    }

    public static byte[] toBytes(String str) {
        return toBytes(str.toCharArray(), getSysCharset());
    }

    public static byte[] toBytes(char[] chars, String charset) {
        if (charset == null || "".equals(charset)) {
            throw new IllegalArgumentException("the charset cannot be empty!!!");
        }
        //
        String csStr = charset.toUpperCase();
        if (!charsets.containsKey(csStr)) {
            synchronized (charsets) {
                if (!charsets.containsKey(csStr)) {
                    charsets.put(csStr, Charset.forName(csStr));
                }
            }
        }
        Charset cs = charsets.get(csStr);
        CharBuffer cb = CharBuffer.wrap(chars);
        ByteBuffer bb = cs.encode(cb);
        byte[] result = new byte[bb.remaining()];
        bb.get(result);
        return result;
    }

    public static byte[] toBytes(char[] chars) {
        return toBytes(chars, getSysCharset());
    }

    public static char[] toChars(byte[] bytes, String charset) {// ���ֽ�תΪ�ַ�(����)
        if (charset == null || "".equals(charset)) {
            throw new IllegalArgumentException("the charset cannot be empty!!!");
        }
        //
        String csStr = charset.toUpperCase();
        if (!charsets.containsKey(csStr)) {
            synchronized (charsets) {
                if (!charsets.containsKey(csStr)) {
                    charsets.put(csStr, Charset.forName(csStr));
                }
            }
        }
        Charset cs = charsets.get(csStr);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        CharBuffer cb = cs.decode(bb);
        char[] result = new char[cb.remaining()];
        cb.get(result);
        return result;
    }

    public static String toString(byte[] bytes, String charset) {
        return new String(toChars(bytes, charset));
    }

    public static String toString(byte[] bytes) {
        return new String(toChars(bytes, getSysCharset()));
    }

    public static char[] toChars(byte[] bytes) {
        return toChars(bytes, getSysCharset());
    }

    /**
     * UTF8_GBK
     *
     * @param bytes
     * @return byte[]
     */
    public static byte[] UTF8_GBK(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("the bytes cannot be null!!!");
        }
        return toBytes(toChars(bytes, "UTF-8"), "GBK");
    }

    /**
     * GBK_UTF8
     *
     * @param bytes
     * @return byte[]
     */
    public static byte[] GBK_UTF8(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("the bytes cannot be null!!!");
        }
        /**toBytes*/
        return toBytes(toChars(bytes, "GBK"), "UTF-8");
    }
}
