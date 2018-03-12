/*
 * Copyright 2017-present, Yudong (Dom) Wang
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.common.RESTTrustManager;
import org.wisdom.tool.model.Charsets;
import org.wisdom.tool.model.HttpMethod;
import org.wisdom.tool.model.HttpReq;
import org.wisdom.tool.model.HttpRsp;

/** 
* @ClassName: RESTClient 
* @Description: Rest Client Class 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public final class RESTClient
{
    private static Logger log = LogManager.getLogger(RESTClient.class);

    // HTTP client
    private CloseableHttpClient hc = null;

    // HTTP client builder
    private HttpClientBuilder cb = null;

    // Request config
    private RequestConfig rc = null;

    // Instance
    private static RESTClient instance = null;

    public static RESTClient getInstance()
    {
        if (instance == null)
        {
            instance = new RESTClient();
        }
        return instance;
    }

    private RESTClient()
    {
        SSLContext sc = null;
        try
        {
            sc = SSLContext.getInstance(RESTConst.TLS);
            TrustManager[] trustAllCrts = new TrustManager[] { new RESTTrustManager() };
            sc.init(null, trustAllCrts, null);
        }
        catch(Exception e)
        {
            log.error("Failed to initialize trust all certificates.", e);
        }

        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String arg0, SSLSession arg1)
            {
                return true;
            }
        };

        rc = RequestConfig.custom()
             .setConnectTimeout(RESTConst.TIME_OUT)
             .setConnectionRequestTimeout(RESTConst.TIME_OUT)
             .setSocketTimeout(RESTConst.TIME_OUT).build();

        this.cb = HttpClients.custom();
        this.cb.setSSLContext(sc);
        this.cb.setSSLHostnameVerifier(hv);

        hc = this.cb.build();
    }

    /**
    * 
    * @Title: exec 
    * @Description: Execute HTTP request 
    * @param @param hreq
    * @param @param ureq
    * @param @return 
    * @return HttpRsp
    * @throws
     */
    private HttpRsp exec(HttpRequestBase req)
    {
        CloseableHttpResponse hr = null;
        HttpRsp rsp = new HttpRsp();

        try
        {
            /* Send HTTP request */
            hr = hc.execute(req);
            HttpEntity he = hr.getEntity();
            if (null != he)
            {
                /* Receive HTTP response */
                String body = EntityUtils.toString(he, Charsets.UTF_8.getCname());
                if (null == body)
                {
                    body = StringUtils.EMPTY;
                }
                rsp.setBody(body);
            }
            else 
            {
                log.warn("HTTP response is null.");
            }

            hr.setReasonPhrase("");
            rsp.setStatus(hr.getStatusLine().toString());
            rsp.setStatusCode(hr.getStatusLine().getStatusCode());
            rsp.setHeaders(new HashMap<String, String>());

            for (Header hdr : hr.getAllHeaders())
            {
                rsp.getHeaders().put(hdr.getName(), hdr.getValue());
            }
        }
        catch(Throwable e)
        {
            log.error("Http request failed.", e);
        } 
        finally
        {
            HttpClientUtils.closeQuietly(hr);
        }

        return rsp;
    }
    
    /**
    * 
    * @Title: req 
    * @Description: Do HTTP request 
    * @param @param req
    * @param @return     
    * @return HttpRsp    
    * @throws
     */
    public HttpRsp exec(HttpReq req)
    {        
        log.info("Start HTTP request: \r\n" + req);
        long time = System.currentTimeMillis();
        HttpRsp rsp = new HttpRsp();
        if (null == req)
        {
            rsp.setRawTxt("HTTP request argument is null. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }

        if (null == req.getMethod())
        {
            rsp.setRawTxt("HTTP method is empty. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }

        if (StringUtils.isEmpty(req.getUrl()))
        {
            rsp.setRawTxt("HTTP URL is empty. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }
        
        try
        {
            /* Set HTTP method */
            HttpRequestBase hrb = null;
            if (HttpMethod.GET.equals(req.getMethod()))
            {
                hrb = new HttpGet(req.getUrl());
            }
            else if (HttpMethod.POST.equals(req.getMethod()))
            {
                hrb = new HttpPost(req.getUrl());
                ((HttpPost) hrb).setEntity(new StringEntity(req.getBody(), Charsets.UTF_8.getCname()));
            }
            else if (HttpMethod.PUT.equals(req.getMethod()))
            {
                hrb = new HttpPut(req.getUrl());
                ((HttpPut) hrb).setEntity(new StringEntity(req.getBody(), Charsets.UTF_8.getCname()));
            }
            else if (HttpMethod.DELETE.equals(req.getMethod()))
            {
                hrb = new HttpDelete(req.getUrl());
            }
            else
            {
                rsp.setRawTxt("Unsupported HTTP method: " + req.getMethod());
                log.error(rsp.getRawTxt());
                return rsp;
            }

            /* Set HTTP headers */
            if (MapUtils.isNotEmpty(req.getHeaders()))
            {
                Map<String, String> hdrs = req.getHeaders();
                Set<Entry<String, String>> es = hdrs.entrySet();
                for (Entry<String, String> e : es)
                {
                    hrb.setHeader(e.getKey(), e.getValue());
                }
            }

            /* Set HTTP cookies */
            if (MapUtils.isNotEmpty(req.getCookies()))
            {
                StringBuilder sb = new StringBuilder();
                Map<String, String> cks = req.getCookies();
                Set<Entry<String, String>> es = cks.entrySet();
                for (Entry<String, String> e : es)
                {
                    sb.append("; ")
                      .append(e.getKey())
                      .append("=")
                      .append(e.getValue());
                }
                String hdrVal = sb.toString().replaceFirst("; ", "");
                hrb.setHeader(RESTConst.COOKIE, hdrVal);
                req.getHeaders().put(RESTConst.COOKIE, hdrVal);
            }

            /* Execute HTTP request */
            hrb.setConfig(rc);
            rsp = this.exec(hrb);
            rsp.setRawTxt(req.toRawTxt() + rsp.toRawTxt());
        }
        catch(Throwable e)
        {
            rsp.setRawTxt("Failed to process this HTTP request: \r\n" + req + "\r\nResponse messages from server: \r\n" + e.getMessage());
            log.error("Failed to process this HTTP request: \r\n" + req, e);
        }

        rsp.setTime(System.currentTimeMillis() - time);
        log.info("Done HTTP request: \r\n" + rsp);
        return rsp;
    }

    /**
    * 
    * @Title: close 
    * @Description: Close REST client connection
    * @param  
    * @return void
    * @throws
     */
    public void close()
    {
        if (null == this.hc)
        {
            return;
        }

        try
        {
            this.hc.close();
            hc = this.cb.build();
        }
        catch(IOException e)
        {
            log.error("Failed to close connection.", e);
        }
    }
}
