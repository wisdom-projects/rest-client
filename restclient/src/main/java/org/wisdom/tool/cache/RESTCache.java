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
package org.wisdom.tool.cache;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.model.Causes;
import org.wisdom.tool.model.HttpHist;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: RESTCache 
* @Description: REST cache
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com
* @Date: 2017-07-27 PM 5:01:37 
* @Version: Wisdom RESTClient V1.3 
*/
public class RESTCache
{
    /**
     * HTTP histories
     */
    private static Map<String, HttpHist> hists = new LinkedHashMap<String, HttpHist>();

    /**
     * Test cause
     */
    private static Causes cs = null;

    /**
     * Check if it's CLI running
     */
    private static boolean isCLIRunning;

    public static boolean isCLIRunning()
    {
        return isCLIRunning;
    }

    public static void setCLIRunning(boolean isCLIRunning)
    {
        RESTCache.isCLIRunning = isCLIRunning;
    }

    public static Map<String, HttpHist> getHists()
    {
        return hists;
    }

    public static Causes getCauses()
    {
        if (null == cs)
        {
            InputStream is = RESTUtil.getInputStream(RESTConst.CAUSE_JSON);
            cs = RESTUtil.toOject(is, Causes.class);
            RESTUtil.close(is);
            if (null == cs || MapUtils.isEmpty(cs.getCauses()))
            {
                return null;
            }
        }
        return cs;
    }

}
