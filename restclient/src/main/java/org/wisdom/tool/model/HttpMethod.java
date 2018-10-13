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
* @ClassName: HttpMethod 
* @Description: HTTP method 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public enum HttpMethod
{
    GET(0), POST(1), PUT(2), DELETE(3);
    
    private int mid;

    private HttpMethod(int mid)
    {
        this.mid = mid;
    }

    /** 
    * @return mid 
    */
    public int getMid()
    {
        return mid;
    }

    /**
     * @param mid the mid to set
     */
    public void setMid(int mid)
    {
        this.mid = mid;
    }
    
}
