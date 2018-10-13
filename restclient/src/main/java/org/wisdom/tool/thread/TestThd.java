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

import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.util.TestUtil;

/** 
* @ClassName: TestThd 
* @Description: Test thread 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-18 PM 9:41:08  
* @Version: Wisdom RESTClient V1.2 
*/
public class TestThd extends Thread
{
    private HttpHists hists = null;

    public TestThd(HttpHists hists)
    {
        this.hists = hists;
    }

    public HttpHists getHists()
    {
        return hists;
    }

    public void setHists(HttpHists hists)
    {
        this.hists = hists;
    }

    public void run()
    {
        if (null == hists)
        {
            return;
        }
        TestUtil.runTest(hists);
    }
}
