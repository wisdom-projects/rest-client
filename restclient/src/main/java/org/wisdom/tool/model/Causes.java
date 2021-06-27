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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
* @ClassName: Causes 
* @Description: Test causes 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2016-12-25 PM 8:21:08 
* @Version: Wisdom RESTClient V1.3 
*/
public class Causes implements Serializable
{
    private static final long serialVersionUID = 5746546932645175308L;

    @JsonProperty("messages")
    private Map<Integer, Cause> causes = null;

    private Integer total = 0;

    public Causes()
    {
        if (null != causes)
        {
            this.total = this.causes.size();
        }
    }

    public Causes(Map<Integer, Cause> causes)
    {
        this.causes = causes;
        if (null != causes)
        {
            this.total = this.causes.size();
        }
    }

    public Map<Integer, Cause> getCauses()
    {
        return causes;
    }

    public void setCauses(Map<Integer, Cause> causes)
    {
        this.causes = causes;
        if (null != causes)
        {
            this.total = this.causes.size();
        }
    }

    public Integer getTotal()
    {
        return total;
    }

    public void setTotal(Integer total)
    {
        this.total = total;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Causes [causes=");
        builder.append(causes);
        builder.append(", total=");
        builder.append(total);
        builder.append("]");
        return builder.toString();
    }

}
