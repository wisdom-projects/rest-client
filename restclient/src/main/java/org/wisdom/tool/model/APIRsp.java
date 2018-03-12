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
* @ClassName: APIRsp 
* @Description: RESTful API response 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class APIRsp implements Serializable
{
    private static final long serialVersionUID = 4725797171705482898L;

    /**
     * API response status
     */
    private String status;

    /**
     * API response code
     */
    private Integer code;

    /**
     * API response message
     */
    private String message;

    /**
     * API response model
     */
    private String model;
    
    /**
     * API response example
     */
    private String example;

    public APIRsp()
    {
    }
    
    public APIRsp(HttpRsp rsp)
    {
        if (null == rsp)
        {
            return;
        }

        if (StringUtils.isEmpty(rsp.getBody()))
        {
            this.model = RESTConst.NA;
            this.example = RESTConst.NA;
        }
        else
        {
            this.model = StringEscapeUtils.escapeHtml(RESTUtil.toModel(rsp.getBody()));
            this.example = StringEscapeUtils.escapeHtml(RESTUtil.format(rsp.getBody()));
        }

        this.status = rsp.getStatus();
        this.code = rsp.getStatusCode();
        if (null == this.code)
        {
            this.code = 0;
        }
        this.message = APIUtil.getReason(this.code);
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
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
        builder.append("APIRep [status=");
        builder.append(status);
        builder.append(", code=");
        builder.append(code);
        builder.append(", message=");
        builder.append(message);
        builder.append(", model=");
        builder.append(model);
        builder.append(", example=");
        builder.append(example);
        builder.append("]");
        return builder.toString();
    }
    
   
}
