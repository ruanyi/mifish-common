package com.mifish.common.util;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 19:02
 */
public final class HttpClientUtil {

    /***poolingHttpClientConnectionManager*/
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //配置时同时支持http、https
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        //初始化连接管理器
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //最大连接数200
        poolingHttpClientConnectionManager.setMaxTotal(200);
        //设置最大路由器
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);
    }

    /***
     *
     * 创建一个安全的httpclient对象
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
        /*CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接*/
        return httpClient;
    }

    /***HttpClientUtil*/
    private HttpClientUtil() throws Exception {
        throw new IllegalAccessException("HttpClientUtil cannot be init");
    }
}
