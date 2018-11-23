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
package org.wisdom.tool.gui.req;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.commons.lang.StringUtils;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.RESTView;
import org.wisdom.tool.gui.common.TabModel;
import org.wisdom.tool.gui.util.UIUtil;

/** 
* @ClassName: ReqTabPanel 
* @Description: Request table panel 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@qq.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class ReqTabPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = -1299418241312495718L;

    private TabModel tabMdl = null;

    private JTable tab = null;

    private JLabel lblKey = null;

    private JLabel lblVal = null;

    private JTextField txtFldKey = null;

    private JTextField txtFldVal = null;

    private JButton btnAdd = null;

    private JButton btnDel = null;

    private ImageIcon iconAdd = null;

    private ImageIcon iconDel = null;

    private JPopupMenu pm = null;

    private JMenuItem miRmSel = null;

    private JMenuItem miRmAll = null;
    
    private MouseAdapter ma = new MouseAdapter()
    {
        private void popup(MouseEvent e)
        {
            int rc = tab.getRowCount();
            if (rc < 1)
            {
                miRmAll.setEnabled(false);
                miRmSel.setEnabled(false);
                return;
            }

            rc = tab.getSelectedRowCount();
            if (rc < 1)
            {
                miRmAll.setEnabled(true);
                miRmSel.setEnabled(false);
            }
            else
            {
                miRmAll.setEnabled(true);
                miRmSel.setEnabled(true);
            }

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
    
    public ReqTabPanel(String name)
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
        tab.addMouseListener(ma);
        tab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        miRmSel = new JMenuItem(RESTConst.RM_SEL);
        miRmSel.setName(RESTConst.RM_SEL);
        miRmSel.addActionListener(this);

        miRmAll = new JMenuItem(RESTConst.RM_ALL);
        miRmAll.setName(RESTConst.RM_ALL);
        miRmAll.addActionListener(this);

        pm = new JPopupMenu();
        pm.add(miRmSel);
        pm.add(miRmAll);
        
        txtFldKey = new JTextField(RESTConst.FIELD_SIZE);
        txtFldVal = new JTextField(RESTConst.FIELD_SIZE);

        lblKey = new JLabel(RESTConst.KEY + ":");
        lblVal = new JLabel(RESTConst.VALUE + ":");

        iconAdd = UIUtil.getIcon(RESTConst.ICON_ADD);
        iconDel = UIUtil.getIcon(RESTConst.ICON_DEL);

        btnAdd = new JButton(iconAdd);
        btnAdd.setName(RESTConst.ADD);
        btnAdd.setToolTipText(RESTConst.ADD + " " + name);
        btnAdd.addActionListener(this);

        btnDel = new JButton(iconDel);
        btnDel.setName(RESTConst.DELETE);
        btnDel.setToolTipText(RESTConst.DELETE + " " + name);
        btnDel.addActionListener(this);

        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlNorth.add(lblKey);
        pnlNorth.add(txtFldKey);
        pnlNorth.add(lblVal);
        pnlNorth.add(txtFldVal);
        pnlNorth.add(btnAdd);
        pnlNorth.add(btnDel);

        this.add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new GridLayout(1, 1));
        JScrollPane spHdr = new JScrollPane(tab);
        pnlCenter.add(spHdr);

        this.add(pnlCenter, BorderLayout.CENTER);
    }
    
    private void btnPerformed(Object src)
    {
        if (!(src instanceof JButton))
        {
            return;
        }
        JButton btn = (JButton) src;
        if (RESTConst.ADD.equals(btn.getName()))
        {
            String key = txtFldKey.getText();
            String value = txtFldVal.getText();
            if (StringUtils.isBlank(key))
            {
                return;
            }

            tabMdl.insertRow(key, value);
            txtFldKey.setText(StringUtils.EMPTY);
            txtFldVal.setText(StringUtils.EMPTY);
            txtFldKey.requestFocus();
            return;
        }

        if (RESTConst.DELETE.equals(btn.getName()))
        {
            int rc = tab.getSelectedRowCount();
            if (rc < 1)
            {
                return;
            }

            int[] rows = tab.getSelectedRows();
            tabMdl.deleteRow(rows);
        }
    }

    private void menuPerformed(Object src)
    {
        if (!(src instanceof JMenuItem))
        {
            return;
        }
        JMenuItem item = (JMenuItem) (src);
        if (RESTConst.RM_SEL.equals(item.getName()))
        {
            int rc = tab.getSelectedRowCount();
            if (rc < 1)
            {
                return;
            }

            int[] rows = tab.getSelectedRows();
            tabMdl.deleteRow(rows);
            return;
        }

        if (RESTConst.RM_ALL.equals(item.getName()))
        {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RESTView.getView(),
                                                    RESTConst.CONFIRM_RM_ALL, 
                                                    RESTConst.RM_ALL,
                                                    JOptionPane.YES_NO_OPTION);
            if (0 == ret)
            {
                tabMdl.clear();
            }
            return;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        this.btnPerformed(e.getSource());
        this.menuPerformed(e.getSource());
    }

}
