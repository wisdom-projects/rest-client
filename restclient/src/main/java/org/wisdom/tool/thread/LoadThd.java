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

package org.wisdom.tool.thread;

import org.wisdom.tool.RESTMain;
import org.wisdom.tool.constant.RESTConst;

/** 
* @Class Name : LoadThd 
* @Description: Load historical data thread 
* @Author     : Dom Wang 
* @Email      : wisdomtool@qq.com
* @Date       : Feb 3, 2018 8:00:34 PM 
* @Version    : Wisdom RESTClient V1.3
*/
public class LoadThd extends Thread
{
    private String path = RESTConst.EMPTY;
    
    /** 
    * @Title      : LoadThd 
    * @Description: New load thread 
    * @Param      : @param path
    */
    public LoadThd(String path)
    {
        this.path = path;
    }

    public void run()
    {
        RESTMain.load(path);
        RESTMain.init();
    }

}
