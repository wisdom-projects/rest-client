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

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.RESTView;
import org.wisdom.tool.gui.req.ReqView;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.util.RESTClient;

/** 
* @ClassName: RESTThd 
* @Description: Test thread 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-18 PM 9:41:08 
* @Version: Wisdom RESTClient V1.2 
*/
public class RESTThd extends Thread
{
    private static Logger log = LogManager.getLogger(RESTThd.class);

    public void interrupt()
    {
        try
        {
            RESTClient.getInstance().close();
            super.interrupt();
        }
        catch(Throwable e)
        {
            log.error("Failed to interrupt thread.", e);
        }
    }

    public void run()
    {
        ReqView rv = RESTView.getView().getReqView();
        UIUtil.submit(rv);

        rv.getBtnStart().setIcon(rv.getIconStart());
        rv.getBtnStart().setToolTipText(RESTConst.START);
        rv.getBtnStart().setEnabled(true);

        rv.getProgressBar().setVisible(false);
        rv.getProgressBar().setIndeterminate(false);

        String body = RESTView.getView().getRspView().getBodyView().getTxtAra().getText();
        RESTView.getView().getTabPane().setSelectedIndex(1);
        if (StringUtils.isNotEmpty(body))
        {
            RESTView.getView().getRspView().getTabPane().setSelectedIndex(0);
        }
        else
        {
            RESTView.getView().getRspView().getTabPane().setSelectedIndex(2);
        }

    }
}
