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
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: RspBodyPanel 
* @Description: Response body panel 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class RspTextPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 5120996065049850894L;

    private JTextArea txtAra = null;

    private JPopupMenu pm = null;

    private JMenuItem miFmt = null;

    private JMenuItem miCpy = null;

    private MouseAdapter ma = new MouseAdapter()
    {
        private void popup(MouseEvent e)
        {
            if (!txtAra.isEnabled() || StringUtils.isBlank(txtAra.getText()))
            {
                miFmt.setEnabled(false);
                miCpy.setEnabled(false);
                return;
            }

            txtAra.requestFocus();
            if (RESTConst.RAW.equals(txtAra.getName()))
            {
                miFmt.setEnabled(false);
            }
            else
            {
                miFmt.setEnabled(true);
            }

            miCpy.setEnabled(true);
            if (e.isPopupTrigger())
            {
                pm.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            this.popup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            this.popup(e);
        }
    };

    public RspTextPanel(String name)
    {
        this.init(name);
    }

    public JTextArea getTxtAra()
    {
        return txtAra;
    }

    /**
    * 
    * @Title: init 
    * @Description: Component Initialization 
    * @param
    * @return void 
    * @throws
     */
    private void init(String name)
    {
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));

        txtAra = new JTextArea(RESTConst.AREA_ROWS, 1);
        txtAra.setName(name);
        txtAra.setEditable(false);
        txtAra.addMouseListener(ma);

        miFmt = new JMenuItem(RESTConst.FORMAT);
        miFmt.setName(RESTConst.FORMAT);
        miFmt.addActionListener(this);

        miCpy = new JMenuItem(RESTConst.COPY);
        miCpy.setName(RESTConst.COPY);
        miCpy.addActionListener(this);

        pm = new JPopupMenu();
        pm.add(miCpy);
        pm.addSeparator();
        pm.add(miFmt);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(txtAra);
        pnlCenter.add(sp);

        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (!(src instanceof JMenuItem))
        {
            return;
        }

        if (StringUtils.isBlank(txtAra.getText()))
        {
            return;
        }

        JMenuItem item = (JMenuItem) (src);
        if (RESTConst.FORMAT.equals(item.getName()))
        {
            String body = RESTUtil.format(txtAra.getText());
            txtAra.setText(body);
            return;
        }

        if (RESTConst.COPY.equals(item.getName()))
        {
            StringSelection ss = null;
            String seltxt = txtAra.getSelectedText();
            if (StringUtils.isNotBlank(seltxt))
            {
                ss = new StringSelection(seltxt);
            }
            else
            {
                ss = new StringSelection(txtAra.getText());
            }

            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(ss, null);

        }
    }

}
