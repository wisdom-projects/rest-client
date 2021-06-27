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
* @ClassName: APIDoc 
* @Description: RESTful API Document model 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public class APIDoc implements Serializable
{
    private static final long serialVersionUID = 6134938535376261029L;

    /**
     * API description
     */
    @JsonProperty("api_description")
    private APIDesc desc;

    /**
     * API list
     */
    @JsonProperty("api_list")
    private List<APIItem> apiLst;

    public APIDesc getDesc()
    {
        return desc;
    }

    public void setDesc(APIDesc desc)
    {
        this.desc = desc;
    }

    public List<APIItem> getApiLst()
    {
        return apiLst;
    }

    public void setApiLst(List<APIItem> apiLst)
    {
        this.apiLst = apiLst;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("APIDoc [desc=");
        builder.append(desc);
        builder.append(", apiLst=");
        builder.append(apiLst);
        builder.append("]");
        return builder.toString();
    }

}
