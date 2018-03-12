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
package org.wisdom.tool.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.hist.HistView;
import org.wisdom.tool.gui.req.ReqView;
import org.wisdom.tool.gui.rsp.RspView;

/** 
 * @ClassName: RESTView 
 * @Description: Rest view frame
 * @Author: Yudong (Dom) Wang
 * @Email: wisdomtool@outlook.com 
 * @Date: Jan 20, 2017 12:30:29 PM 
 * @Version: Wisdom RESTClient V1.2 
 */
public class RESTView extends JPanel
{
    private static final long serialVersionUID = 957993921065702646L;

    private static RESTView view = null;
    
    private ReqView vReq = null;

    private RspView vRsp = null;

    private HistView vHist = null;

    private JTabbedPane tp = null;
    
    public RESTView()
    {
        this.init();
    }

    public static RESTView getView()
    {
        if (null == view)
        {
            view = new RESTView();
        }
        return view;
    }

    public ReqView getReqView()
    {
        return vReq;
    }

    public RspView getRspView()
    {
        return vRsp;
    }

    public HistView getHistView()
    {
        return vHist;
    }

    public JTabbedPane getTabPane()
    {
        return tp;
    }

    /**
    * 
    * @Title: init 
    * @Description: Component Initialization 
    * @param
    * @return void 
    * @throws
     */
    private void init()
    {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));
        this.setBorder(BorderFactory.createEmptyBorder(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        vReq = new ReqView();
        vRsp = new RspView();
        vHist = new HistView();

        tp = new JTabbedPane();
        tp.add(RESTConst.REQUEST, vReq);
        tp.add(RESTConst.RESPONSE, vRsp);
        tp.add(RESTConst.HIST, vHist);

        this.add(tp);

        this.setBorder(BorderFactory.createTitledBorder(null, RESTConst.REST_CLIENT, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }
}
