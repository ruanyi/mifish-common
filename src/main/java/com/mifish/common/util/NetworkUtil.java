package com.mifish.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 21:59
 */
public final class NetworkUtil {

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            Enumeration<NetworkInterface> itfs = NetworkInterface.getNetworkInterfaces();
            while (itfs.hasMoreElements()) {
                NetworkInterface itf = itfs.nextElement();
                Enumeration<InetAddress> addres = itf.getInetAddresses();
                while (addres.hasMoreElements()) {
                    InetAddress addr = addres.nextElement();
                    if (addr instanceof Inet4Address && !"127.0.0.1".equals(addr.getHostAddress())) {
                        return addr.getHostAddress();
                    }
                }
            }
            return "";
        } catch (SocketException e) {
            return "";
        }
    }

    private NetworkUtil() {

    }
}
