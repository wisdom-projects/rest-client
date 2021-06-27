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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.model.HttpMethod;
import org.wisdom.tool.model.HttpReq;
import org.wisdom.tool.model.HttpRsp;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/** 
* @ClassName: RESTClient 
* @Description: Rest Client Class 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public final class RESTClient
{
    private static Logger log = LogManager.getLogger(RESTClient.class);

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

    private HttpRsp result(Response resp)
    {
        HttpRsp result = new HttpRsp();
        try
        {
            result.setBody(resp.body().string());
            result.setStatusCode(resp.code());
            result.setMessage(resp.message());
            result.setHeaders(new HashMap<String, String>());

            Headers hdrs = resp.headers();
            for (String name : hdrs.names())
            {
                result.getHeaders().put(name, hdrs.get(name));
            }
        }
        catch(IOException e)
        {
            log.error("Http request failed.", e);
        } 
        return result;
    }

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

        Request request = null; 
        Request.Builder builder = new Request.Builder()
                                             .url(req.getUrl());
        try
        {
            String bodyTxt = req.getBody();
            if (null == bodyTxt)
            {
                bodyTxt = "";
            }

            RequestBody body = RequestBody.create(bodyTxt.getBytes());
            
            /* Set HTTP headers */
            if (MapUtils.isNotEmpty(req.getHeaders()))
            {
                Map<String, String> hdrs = req.getHeaders();
                Set<Entry<String, String>> es = hdrs.entrySet();
                for (Entry<String, String> e : es)
                {
                    builder.addHeader(e.getKey(), e.getValue());
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
                builder.addHeader(RESTConst.COOKIE, hdrVal);
                req.getHeaders().put(RESTConst.COOKIE, hdrVal);
            }

            /* Set HTTP method */
            if (HttpMethod.GET.equals(req.getMethod()))
            {
                request = builder.build();
            }
            else if (HttpMethod.POST.equals(req.getMethod()))
            {
                request = builder.post(body).build();
            }
            else if (HttpMethod.PUT.equals(req.getMethod()))
            {
                request = builder.put(body).build();
            }
            else if (HttpMethod.DELETE.equals(req.getMethod()))
            {
                request = builder.delete(body).build();
            }
            else
            {
                rsp.setRawTxt("Unsupported HTTP method: " + req.getMethod());
                log.error(rsp.getRawTxt());
                return rsp;
            }

            Response resp = HTTPClient.client()
                                      .newCall(request)
                                      .execute();
            rsp = this.result(resp);
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
}
