/*
 * Copyright 2021-present, Yudong (Dom) Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wisdom.tool.util;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.common.RESTTrustManager;
import org.wisdom.tool.constant.RESTConst;

import okhttp3.OkHttpClient;

/**
* 
* @Class Name : HTTPClient 
* @Description: HTTP client Utility
* @Author     : Yudong (Dom) Wang 
* @Date       : 2021-6-26 AM 9:59:55 
* @Version    : Wisdom RESTClient V1.3 
*
 **/
public class HTTPClient
{
    private static Logger log = LogManager.getLogger(HTTPClient.class);

    private static OkHttpClient httpClient = null;

    private static TrustManager[] getTrustManager()
    {
        return new TrustManager[] { new RESTTrustManager() };
    }

    public static SSLSocketFactory sslSocketFactory()
    {
        SSLSocketFactory ssFactory = null;
        try
        {
            SSLContext sslContext = SSLContext.getInstance(RESTConst.TLS);
            sslContext.init(null, getTrustManager(), new SecureRandom());
            ssFactory = sslContext.getSocketFactory();
        }
        catch(Exception e)
        {
            log.error("Failed to get SSL socket factory.", e);
        }
        return ssFactory;
    }

    public static HostnameVerifier hostnameVerifier()
    {
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String arg0, SSLSession arg1)
            {
                return true;
            }
        };
        return hv;
    }

    public static X509TrustManager x509TrustManager()
    {
        X509TrustManager xtm = null;
        try
        {
            TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init((KeyStore) null);

            TrustManager[] tms = tmFactory.getTrustManagers();
            if (tms.length > 0 && tms[0] instanceof X509TrustManager)
            {
                xtm = (X509TrustManager) tms[0];
            }
        }
        catch(Exception e)
        {
            log.error("Failed to get X509 trust manager.", e);
        }
        return xtm;
    }

    public static OkHttpClient client()
    {
        if (null != httpClient)
        {
            return httpClient;
        }

        httpClient = new OkHttpClient.Builder()
                     .readTimeout(RESTConst.TIME_60S, TimeUnit.SECONDS)
                     .connectTimeout(RESTConst.TIME_60S, TimeUnit.SECONDS)
                     .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                     .hostnameVerifier(hostnameVerifier())
                     .build();
        return httpClient;
    }
}
