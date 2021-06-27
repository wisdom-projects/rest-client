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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.common.TabModel;

/** 
* @ClassName: RspTabPanel 
* @Description: Response table panel 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.3 
*/
public class RspTabPanel extends JPanel
{
    private static final long serialVersionUID = -1299418241312495718L;

    private TabModel tabMdl = null;

    private JTable tab = null;

    public RspTabPanel(String name)
    {
        this.init(name);
    }

    public TabModel getTabMdl()
    {
        return tabMdl;
    }

    /**
    * 
    * @Title: init 
    * @Description: Component Initialization 
    * @param  name
    * @return void 
    * @throws
     */
    private void init(String name)
    {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));

        List<String> colNames = new ArrayList<String>();
        colNames.add(name);
        colNames.add(RESTConst.VALUE);

        tabMdl = new TabModel(colNames);
        tab = new JTable(tabMdl);
        tab.setFillsViewportHeight(true);
        tab.setAutoCreateRowSorter(false);
        tab.getTableHeader().setReorderingAllowed(false);
        
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JScrollPane sp = new JScrollPane(tab);
        pnlCenter.add(sp);

        this.add(pnlCenter, BorderLayout.CENTER);
    }
    
}
