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

/** 
* @ClassName: ErrCode 
* @Description: HTTP test error code 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-18 PM 3:30:01 
* @Version: Wisdom RESTClient V1.2 
*/
public enum ErrCode
{
    SUCCESS(10000),

    NO_CASE(10001),

    BAD_CASE(10002),

    INCONSISTENT_STATUS(10003),

    INCONSISTENT_BODY(10004),

    HTTP_REQUEST_FAILED(10005),

    BAD_JSON(10006);

    private int code;

    private ErrCode(int c)
    {
        this.code = c;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

}
