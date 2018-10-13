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

import org.wisdom.tool.apidoc.APIUtil;

/** 
* @ClassName: APISum 
* @Description: RESTful API summary 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class APISum implements Serializable
{
    private static final long serialVersionUID = 5656937659315612005L;

    /**
     * API title
     */
    private String title;

    /**
     * API method
     */
    private String method;

    /**
     * API path
     */
    private String path;

    public APISum()
    {
    }
    
    public APISum(HttpReq req)
    {
        if (null == req)
        {
            return;
        }

        this.path = APIUtil.getReqPath(req.getUrl());
        this.title = APIUtil.getTitle(req.getMethod(), APIUtil.getShortPath(req.getUrl()));
        this.method = req.getMethod().name();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String apiKey()
    {
        return this.method + this.path;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("APISumry [title=");
        builder.append(title);
        builder.append(", method=");
        builder.append(method);
        builder.append(", path=");
        builder.append(path);
        builder.append("]");
        return builder.toString();
    }
   
}
