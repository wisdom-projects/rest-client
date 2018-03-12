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
package org.wisdom.tool.gui.rsp;

import java.awt.BorderLayout;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.model.HttpRsp;

/** 
* @ClassName: RspView 
* @Description: Response view panel 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class RspView extends JPanel
{
    private static final long serialVersionUID = -1299418241312495718L;

    private JLabel lblStat = null;
    
    private JTextField txtFldStat = null;
    
    private RspTabPanel pnlHdr = null;
    
    private RspTextPanel pnlBody = null;
    
    private RspTextPanel pnlRaw = null;
    
    private JTabbedPane tp = null;

    public RspView()
    {
        this.init();
    }

    public JTabbedPane getTabPane()
    {
        return tp;
    }

    public RspTextPanel getBodyView()
    {
        return pnlBody;
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

        lblStat = new JLabel(RESTConst.STATUS + ":");
        txtFldStat = new JTextField(RESTConst.FIELD_STATUS_SIZE);
        txtFldStat.setEditable(false);
        
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));
        pnlNorth.add(lblStat, BorderLayout.WEST);
        pnlNorth.add(txtFldStat, BorderLayout.CENTER);
        this.add(pnlNorth, BorderLayout.NORTH);

        tp = new JTabbedPane();
        pnlBody = new RspTextPanel(RESTConst.BODY);
        tp.add(RESTConst.BODY, pnlBody);

        pnlHdr = new RspTabPanel(RESTConst.HEADER);
        tp.add(RESTConst.HEADER, pnlHdr);

        pnlRaw = new RspTextPanel(RESTConst.RAW);
        tp.add(RESTConst.RAW, pnlRaw);

        this.add(tp, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createTitledBorder(null, RESTConst.HTTP_RESPONSE, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
    }

    /**
    * 
    * @Title: reset 
    * @Description: reset response view 
    * @param  
    * @return void
    * @throws
     */
    public void reset()
    {
        txtFldStat.setText(StringUtils.EMPTY);
        pnlRaw.getTxtAra().setText(StringUtils.EMPTY);
        pnlBody.getTxtAra().setText(StringUtils.EMPTY);
        pnlHdr.getTabMdl().clear();
    }

    /**
    * 
    * @Title: setRspView 
    * @Description: Set HTTP response panel view
    * @param @param rsp 
    * @return void
    * @throws
     */
    public void setRspView(HttpRsp rsp)
    {
        if (null == rsp)
        {
            return;
        }

        txtFldStat.setText(rsp.getStatus());
        pnlRaw.getTxtAra().setText(rsp.getRawTxt());
        pnlBody.getTxtAra().setText(rsp.getBody());

        // Set headers
        pnlHdr.getTabMdl().clear();
        if (MapUtils.isNotEmpty(rsp.getHeaders()))
        {
            Set<Entry<String, String>> es = rsp.getHeaders().entrySet();
            for (Entry<String, String> e : es)
            {
                pnlHdr.getTabMdl().insertRow(e.getKey(), e.getValue());
            }
        }
    }

}
