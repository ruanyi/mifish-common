package com.mifish.common.util;

/**
 * BytesUtil
 *
 * @author xsyl
 * @date:2013-12-28
 */
public final class BytesUtil {

    private BytesUtil() {
    }

    /**
     * trim
     *
     * @param bytes
     * @return bytes
     */
    public static byte[] trim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int startloc = 0, endloc = bytes.length - 1;
        boolean lflag = true, rflag = true;
        for (int i = 0, j = endloc; i <= j; ) {
            if (lflag) {
                if (bytes[i] == 32 || bytes[i] == 0) {
                    lflag = true;
                } else {
                    lflag = false;
                    startloc = i;
                }
            }
            if (rflag) {
                if (bytes[j] == 32 || bytes[j] == 0) {
                    rflag = true;
                } else {
                    rflag = false;
                    endloc = j;
                }
            }
            if (i == j || ((lflag == false) && (rflag == false))) {
                break;
            }
            if (lflag) {
                i++;
            }
            if (rflag) {
                j--;
            }
        }
        if (lflag && rflag) {
            return new byte[0];
        }
        byte[] result = new byte[endloc - startloc + 1];
        System.arraycopy(bytes, startloc, result, 0, endloc - startloc + 1);
        return result;
    }

    /**
     * leftTrim
     *
     * @param bytes
     * @return byte[]
     */
    public static byte[] leftTrim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int startloc = 0, len = bytes.length;
        boolean lflag = true;
        for (int i = 0; i < len; i++) {
            if (bytes[i] == 32 || bytes[i] == 0) {
                lflag = true;
                continue;
            } else {
                lflag = false;
                startloc = i;
                break;
            }
        }
        if (lflag) {
            return new byte[0];
        }
        byte[] result = new byte[len - startloc];// endloc-startloc+1,,len-1-startloc+1,,endloc
        // = len-1
        System.arraycopy(bytes, startloc, result, 0, len - startloc);
        return result;
    }

    /**
     * rightTrim
     *
     * @param
     * @return
     */
    public static byte[] rightTrim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int endloc = bytes.length - 1;
        boolean rflag = true;
        for (int j = endloc; j >= 0; j--) {
            if (bytes[j] == 32 || bytes[j] == 0) {
                rflag = true;
                continue;
            } else {
                rflag = false;
                endloc = j;
                break;
            }
        }
        if (rflag) {
            return new byte[0];
        }
        byte[] result = new byte[endloc + 1];// enloc-startloc+1,startloc==0
        System.arraycopy(bytes, 0, result, 0, endloc + 1);
        return result;
    }

    /**
     * read
     *
     * @param bytes
     * @param length
     */
    public static byte[] read(byte[] bytes, int length) {
        byte[] result = new byte[length];
        System.arraycopy(bytes, 0, result, 0, length);
        return result;
    }

    /**
     * read
     *
     * @param bytes
     * @param start
     * @param length
     * @return
     */
    public static byte[] read(byte[] bytes, int start, int length) {
        byte[] result = new byte[length];
        System.arraycopy(bytes, start, result, 0, length);
        return result;
    }

    /**
     * toHexString
     *
     * @param values
     * @return
     */
    public static String toHexString(byte[] values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            byte t = (byte) (values[i] & 0x0F);
            byte k = (byte) ((values[i] >> 4) & 0x0F);
            builder.append(Integer.toHexString(k));
            builder.append(Integer.toHexString(t));
        }
        return builder.toString().toUpperCase();
    }

    /**
     * fromHexString
     *
     * @param value
     * @return
     */
    public static byte[] fromHexString(String value) {
        if (value == null || value.length() == 0) {
            return new byte[]{};
        }
        value = value.toLowerCase();
        char[] cs = value.toCharArray();
        byte[] retVal = new byte[cs.length / 2];
        for (int i = 0; i < cs.length; ) {
            byte h = (byte) (Integer.parseInt(new String(new char[]{cs[i++]}), 16) << 4);
            byte l = (byte) (Integer.parseInt(new String(new char[]{cs[i++]}), 16));
            retVal[i / 2 - 1] = (byte) (h | l);
        }
        return retVal;
    }
}
