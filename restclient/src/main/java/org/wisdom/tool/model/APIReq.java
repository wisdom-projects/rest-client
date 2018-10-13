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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.wisdom.tool.apidoc.APIUtil;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: APIReq 
* @Description: RESTful API request 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class APIReq implements Serializable
{
    private static final long serialVersionUID = 1681002160195520948L;

    /**
     * API request header
     */
    private String header;

    /**
     * API request model
     */
    private String model;

    /**
     * API request example
     */
    private String example;

    public APIReq()
    {
    }
    
    public APIReq(HttpReq req)
    {
        if (null == req)
        {
            return;
        }

        if (StringUtils.isEmpty(req.getBody()))
        {
            this.model = RESTConst.NA;
            this.example = RESTConst.NA;
        }
        else
        {
            this.model = StringEscapeUtils.escapeHtml(RESTUtil.toModel(req.getBody()));
            this.example = StringEscapeUtils.escapeHtml(RESTUtil.format(req.getBody()));
        }

        this.header = APIUtil.headerStr(req.getHeaders());
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getExample()
    {
        return example;
    }

    public void setExample(String example)
    {
        this.example = example;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("APIReq [header=");
        builder.append(header);
        builder.append(", model=");
        builder.append(model);
        builder.append(", example=");
        builder.append(example);
        builder.append("]");
        return builder.toString();
    }
   
}
