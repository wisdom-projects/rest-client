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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
* @ClassName: APIItem 
* @Description: RESTful API item
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public class APIItem implements Serializable
{
    private static final long serialVersionUID = 6748689358510850564L;

    /**
     * API summary
     */
    @JsonProperty("api_summary")
    private APISum sumry;

    /**
     * API request
     */
    @JsonProperty("api_request")
    private APIReq req;

    /**
     * API response list
     */
    @JsonProperty("api_response")
    private List<APIRsp> repLst;

    public APIItem()
    {
    }
    
    public APIItem(APISum sumry, APIReq req, List<APIRsp> repLst)
    {
        this.sumry = sumry;
        this.req = req;
        this.repLst = repLst;
    }

    public APISum getSumry()
    {
        return sumry;
    }

    public void setSumry(APISum sumry)
    {
        this.sumry = sumry;
    }

    public APIReq getReq()
    {
        return req;
    }

    public void setReq(APIReq req)
    {
        this.req = req;
    }

    public List<APIRsp> getRepLst()
    {
        return repLst;
    }

    public void setRepLst(List<APIRsp> repLst)
    {
        this.repLst = repLst;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("APIItem [sumry=");
        builder.append(sumry);
        builder.append(", req=");
        builder.append(req);
        builder.append(", repLst=");
        builder.append(repLst);
        builder.append("]");
        return builder.toString();
    }

}
