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
package org.wisdom.tool.gui.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.util.UIUtil;

/** 
* @ClassName: DonateDialog 
* @Description: Donate Dialog
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class DonateDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = -2821579370172523357L;

    public DonateDialog()
    {
        this.init();
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
        this.setTitle(RESTConst.DONATE_BY_PAY);
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, RESTConst.BORDER_WIDTH));

        JPanel pnlDialog = new JPanel();
        pnlDialog.setLayout(new BorderLayout());

        JLabel lblDonate = new JLabel();
        lblDonate.setIcon(UIUtil.getIcon(RESTConst.DONATE_ICON));
        lblDonate.setToolTipText(RESTConst.DONATE_BY_PAY);

        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlNorth.add(lblDonate);
        pnlDialog.add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JTextPane tp = new JTextPane();
        tp.setEditable(false);
        tp.setContentType("text/html");
        tp.setText(UIUtil.contents(RESTConst.DONATION));
        pnlCenter.add(new JScrollPane(tp));
        pnlDialog.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel();
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton(RESTConst.OK);
        btnOK.addActionListener(this);
        btnOK.requestFocus();
        
        getRootPane().setDefaultButton(btnOK);
        pnlSouth.add(btnOK);
        pnlDialog.add(pnlSouth, BorderLayout.SOUTH);

        this.setContentPane(pnlDialog);
        this.setIconImage(UIUtil.getImage(RESTConst.LOGO));
        this.pack();
    }

    public void actionPerformed(ActionEvent arg0)
    {
        this.setVisible(false);
    }

}
