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
package org.wisdom.tool.except;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.model.Cause;
import org.wisdom.tool.model.Results;

/** 
* @ClassName: RESTException 
* @Description: REST exception
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-25 PM 8:40:33 
* @Version: Wisdom RESTClient V1.2 
*/
public class RESTException extends Exception
{
    private static Logger log = LogManager.getLogger(RESTException.class);

    private static final long serialVersionUID = -8596508991250642705L;

    private String status;

    public RESTException()
    {
    }

    public RESTException(String message)
    {
        super(message);
    }

    public RESTException(Cause c)
    {
        super(c.toString());
        this.setStatus(Results.ERROR.getResult());
        log.error(c.toString());
    }

    public RESTException(Cause c, Results r)
    {
        super(c.toString());
        this.setStatus(r.getResult());
        log.error(c.toString());
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
