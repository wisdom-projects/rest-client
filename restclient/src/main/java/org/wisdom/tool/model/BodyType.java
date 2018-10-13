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
* @ClassName: BodyType 
* @Description: Body type 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public enum BodyType
{
    STRING(0, "String"), FILE(1, "File");

    private int tid;

    private String type;

    private BodyType(int tid, String type)
    {
        this.tid = tid;
        this.type = type;
    }

    /**
     * @return tid
     */
    public int getTid()
    {
        return tid;
    }

    /**
     * @param tid
     *            the tid to set
     */
    public void setTid(int tid)
    {
        this.tid = tid;
    }

    /**
     * @return type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

}
