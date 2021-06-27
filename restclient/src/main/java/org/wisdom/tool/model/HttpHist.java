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
package org.wisdom.tool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
* @ClassName: HttpHist 
* @Description: HTTP history model 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public class HttpHist implements Serializable
{
    private static final long serialVersionUID = 8720504484781387383L;

    /**
     * History Key
     */
    private String key;

    /**
     * HTTP Request
     */
    @JsonProperty("request")
    private HttpReq req;

    /**
     * HTTP Response
     */
    @JsonProperty("response")
    private HttpRsp rsp;

    /**
     * description
     */
    private String descr = "-";

    /**
     * Need assert body
     */
    private Boolean assertBdy;

    /**
     * Test result
     */
    private Results result;
    
    /**
     * The cause of failure or error
     */
    private String cause;

    /**
     * Excluded nodes
     */
    private List<String> excludedNodes;

    public HttpHist()
    {
        this.assertBdy = true;
    }

    public HttpHist(String key, HttpReq req, HttpRsp rsp)
    {
        this.key = key;
        this.req = req;
        this.rsp = rsp;
        this.assertBdy = true;
    }

    public HttpHist(String key, HttpHist hist)
    {
        this.key = key;
        this.req = hist.getReq();
        this.rsp = hist.getRsp();
        this.assertBdy = hist.getAssertBdy();
        this.descr = hist.getDescr();
        this.result = hist.getResult();
        this.cause = hist.getCause();
        this.excludedNodes = hist.getExcludedNodes();
    }

    public HttpHist(HttpHist hist)
    {
        this.req = new HttpReq(hist.getReq());
        this.rsp = new HttpRsp(hist.getRsp());

        this.key = hist.getKey();
        this.cause = hist.getCause();
        this.result = hist.getResult();
        this.assertBdy = hist.getAssertBdy();
        if (null != hist.getDescr())
        {
            this.descr = hist.getDescr();
        }
        this.excludedNodes = hist.getExcludedNodes();
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public HttpReq getReq()
    {
        return req;
    }

    public void setReq(HttpReq req)
    {
        this.req = req;
    }

    public HttpRsp getRsp()
    {
        return rsp;
    }

    public void setRsp(HttpRsp rsp)
    {
        this.rsp = rsp;
    }

    public Results getResult()
    {
        return result;
    }

    public void setResult(Results result)
    {
        this.result = result;
    }

    public Boolean getAssertBdy()
    {
        return assertBdy;
    }

    public void setAssertBdy(Boolean assertBdy)
    {
        this.assertBdy = assertBdy;
    }
    
    public String getCause()
    {
        return cause;
    }

    public void setCause(String cause)
    {
        this.cause = cause;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr(String descr)
    {
        this.descr = descr;
    }

    public List<String> getExcludedNodes()
    {
        return excludedNodes;
    }

    public void setExcludedNodes(List<String> nodes)
    {
        this.excludedNodes = nodes;
    }

    public List<Object> toRow(Object histId)
    {
        List<Object> data = new ArrayList<Object>();
        data.add(histId);
        data.add(this.req.getMethod() + " " + req.getUrl());
        data.add(this.rsp.getStatus());
        data.add(this.rsp.getDate());
        data.add(this.rsp.getTime() + "ms");
        data.add(this.descr);
        return data;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpHist [key=");
        builder.append(key);
        builder.append(", req=");
        builder.append(req);
        builder.append(", rsp=");
        builder.append(rsp);
        builder.append(", descr=");
        builder.append(descr);
        builder.append(", assertBdy=");
        builder.append(assertBdy);
        builder.append(", result=");
        builder.append(result);
        builder.append(", cause=");
        builder.append(cause);
        builder.append(", excludedNodes=");
        builder.append(excludedNodes);
        builder.append("]");
        return builder.toString();
    }

}
