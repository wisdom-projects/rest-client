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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.wisdom.tool.constant.RESTConst;

/** 
* @ClassName: RESTThdPool 
* @Description: REST thread pool 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-18 PM 9:41:08 
* @Version: Wisdom RESTClient V1.2 
*/
public final class RESTThdPool
{
    /**
     * Thread pool
     */
    private ExecutorService pool = Executors.newFixedThreadPool(RESTConst.POOL_SIZE);

    // Pool instance
    private static RESTThdPool instance = null;

    public static RESTThdPool getInstance()
    {
        if (instance == null)
        {
            instance = new RESTThdPool();
        }
        return instance;
    }

    private RESTThdPool()
    {
    }

    public ExecutorService getPool()
    {
        return pool;
    }

}
