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
import java.util.Locale;

import org.wisdom.tool.constant.RESTConst;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/** 
* @ClassName: Cause 
* @Description: Test cause 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-25 PM 7:58:51 
* @Version: Wisdom RESTClient V1.2 
*/
public class Cause implements Serializable
{
    private static final long serialVersionUID = 6630167190284292762L;

    /**
     * Error code
     */
    @JsonInclude(Include.NON_NULL)
    private ErrCode code;

    /**
     * Cause message Zh_CN
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("message_Zh_CN")
    private String msgZhCN;

    /**
     * Cause message En_US
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("message_En_US")
    private String msgEnUS;

    public Cause()
    {
    }

    public Cause(Cause c)
    {
        if (null == c)
        {
            return;
        }

        this.code = c.getCode();
        this.msgZhCN = c.getMsgZhCN();
        this.msgEnUS = c.getMsgEnUS();
    }

    public ErrCode getCode()
    {
        return code;
    }

    public void setCode(ErrCode code)
    {
        this.code = code;
    }

    public String getMsgZhCN()
    {
        return msgZhCN;
    }

    public void setMsgZhCN(String msgZhCN)
    {
        this.msgZhCN = msgZhCN;
    }

    public String getMsgEnUS()
    {
        return msgEnUS;
    }

    public void setMsgEnUS(String msgEnUS)
    {
        this.msgEnUS = msgEnUS;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (code.equals(ErrCode.SUCCESS))
        {
            sb.append("-");
            return sb.toString();
        }

        sb.append("The cause of the error/failure: ");
        sb.append(code.getCode()).append(" -- ");
        if (RESTConst.LANG_ZH.equalsIgnoreCase(Locale.getDefault().getLanguage()))
        {
            sb.append(msgZhCN);
        }
        else
        {
            sb.append(msgEnUS);
        }

        return sb.toString();
    }

}
