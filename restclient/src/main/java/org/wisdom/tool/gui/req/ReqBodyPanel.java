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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.model.BodyType;
import org.wisdom.tool.model.Charsets;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: ReqBodyPanel 
* @Description: Request body panel 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com 
* @Date: 2017-07-22 PM 10:42:57 
* @Version: Wisdom RESTClient V1.2 
*/
public class ReqBodyPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 5120996065049850894L;

    private JLabel lblBodyType = null;

    private JLabel lblContentType = null;

    private JLabel lblCharset = null;

    private JComboBox<String> cbContentType = null;

    private JComboBox<String> cbCharset = null;

    private JComboBox<String> cbBodyType = null;

    private JTextArea txtAraBody = null;

    private JTextField txtFldPath = null;

    private JButton btnLoadFile = null;

    private JPanel pnlSouth = null;

    private JPanel pnlCenter = null;

    private JPanel pnlNorth = null;

    private JFileChooser fc = null;

    private JPopupMenu pm = null;

    private JMenuItem miFmt = null;

    private JMenuItem miCpy = null;

    private JMenuItem miPst = null;

    private JMenuItem miClr = null;

    private MouseAdapter ma = new MouseAdapter()
    {
        private void popup(MouseEvent e)
        {
            txtAraBody.requestFocus();
            miPst.setEnabled(true);
            if (!txtAraBody.isEnabled() || StringUtils.isBlank(txtAraBody.getText()))
            {
                miFmt.setEnabled(false);
                miCpy.setEnabled(false);
                miClr.setEnabled(false);
            }
            else
            {
                miFmt.setEnabled(true);
                miCpy.setEnabled(true);
                miClr.setEnabled(true);
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
    
    public ReqBodyPanel()
    {
        this.init();
    }

    public JComboBox<String> getCbContentType()
    {
        return cbContentType;
    }

    public JComboBox<String> getCbCharset()
    {
        return cbCharset;
    }

    public JTextArea getTxtAraBody()
    {
        return txtAraBody;
    }

    public JTextField getTxtFldPath()
    {
        return txtFldPath;
    }

    public JComboBox<String> getCbBodyType()
    {
        return cbBodyType;
    }

    public JButton getBtnLoadFile()
    {
        return btnLoadFile;
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
        this.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));

        Vector<String> vtContType = new Vector<String>();
        vtContType.add(ContentType.APPLICATION_JSON.getMimeType());
        vtContType.add(ContentType.APPLICATION_XML.getMimeType());
        vtContType.add(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        vtContType.add(ContentType.TEXT_PLAIN.getMimeType());
        vtContType.add(ContentType.TEXT_XML.getMimeType());
        vtContType.add(ContentType.TEXT_HTML.getMimeType());
        vtContType.add(ContentType.MULTIPART_FORM_DATA.getMimeType());
        vtContType.add(ContentType.APPLICATION_XHTML_XML.getMimeType());
        vtContType.add(ContentType.APPLICATION_ATOM_XML.getMimeType());
        vtContType.add(ContentType.APPLICATION_SVG_XML.getMimeType());
        vtContType.add(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        
        Vector<String> vtChrset = new Vector<String>();
        vtChrset.add(Charsets.UTF_8.getCname());
        vtChrset.add(Charsets.US_ASCII.getCname());
        vtChrset.add(Charsets.ISO_8859_1.getCname());
        vtChrset.add(Charsets.UTF_16.getCname());
        vtChrset.add(Charsets.UTF_16LE.getCname());
        vtChrset.add(Charsets.UTF_16BE.getCname());

        Vector<String> vtBdyType = new Vector<String>();
        vtBdyType.add(BodyType.STRING.getType());
        vtBdyType.add(BodyType.FILE.getType());

        cbContentType = new JComboBox<String>(vtContType);
        cbContentType.setEditable(true);
        cbContentType.setToolTipText(RESTConst.CONTENT_TYPE.replace("-", " "));

        cbCharset = new JComboBox<String>(vtChrset);
        cbCharset.setEditable(true);
        cbCharset.setToolTipText(RESTConst.CHARSET);

        cbBodyType = new JComboBox<String>(vtBdyType);
        cbBodyType.setToolTipText(RESTConst.BODY_TYPE.replace("-", " "));
        cbBodyType.setEnabled(false);
        cbBodyType.addActionListener(this);

        btnLoadFile = new JButton(RESTConst.BROWSE);
        btnLoadFile.setName(RESTConst.BROWSE);
        btnLoadFile.setToolTipText(RESTConst.SELECT_FILE);
        btnLoadFile.addActionListener(this);

        txtFldPath = new JTextField(RESTConst.FIELD_PATH_SIZE);
        txtFldPath.setToolTipText(RESTConst.FILE_PATH);

        txtAraBody = new JTextArea(RESTConst.AREA_ROWS, 1);
        txtAraBody.setToolTipText(RESTConst.BODY_CONTENT);
        txtAraBody.setEnabled(false);
        txtAraBody.setBackground(UIUtil.lightGray());
        txtAraBody.addMouseListener(ma);

        lblBodyType = new JLabel(RESTConst.BODY_TYPE + ":");
        lblContentType = new JLabel(RESTConst.CONTENT_TYPE + ":");
        lblCharset = new JLabel(RESTConst.CHARSET + ":");

        miFmt = new JMenuItem(RESTConst.FORMAT);
        miFmt.setName(RESTConst.FORMAT);
        miFmt.addActionListener(this);

        miCpy = new JMenuItem(RESTConst.COPY);
        miCpy.setName(RESTConst.COPY);
        miCpy.addActionListener(this);

        miClr = new JMenuItem(RESTConst.CLEAR);
        miClr.setName(RESTConst.CLEAR);
        miClr.addActionListener(this);

        miPst = new JMenuItem(RESTConst.PASTE);
        miPst.setName(RESTConst.PASTE);
        miPst.addActionListener(this);

        pm = new JPopupMenu();
        pm.add(miCpy);
        pm.addSeparator();
        pm.add(miPst);
        pm.addSeparator();
        pm.add(miFmt);
        pm.addSeparator();
        pm.add(miClr);

        JPanel pnlbdyType = new JPanel();
        pnlbdyType.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlbdyType.add(lblBodyType);
        pnlbdyType.add(cbBodyType);

        JPanel pnlCntType = new JPanel();
        pnlCntType.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlCntType.add(lblContentType);
        pnlCntType.add(cbContentType);

        JPanel pnlChrstType = new JPanel();
        pnlChrstType.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlChrstType.add(lblCharset);
        pnlChrstType.add(cbCharset);

        pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));
        pnlNorth.add(pnlbdyType, BorderLayout.WEST);
        pnlNorth.add(pnlCntType, BorderLayout.CENTER);
        pnlNorth.add(pnlChrstType, BorderLayout.EAST);
        this.add(pnlNorth, BorderLayout.NORTH);

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());
        JScrollPane spBody = new JScrollPane(txtAraBody);
        pnlCenter.add(spBody);
        this.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlLoadFile = new JPanel();
        pnlLoadFile.setLayout(new BorderLayout(RESTConst.BORDER_WIDTH, 0));
        pnlLoadFile.add(txtFldPath, BorderLayout.CENTER);
        pnlLoadFile.add(btnLoadFile, BorderLayout.EAST);
        
        pnlSouth = new JPanel();
        pnlSouth.setLayout(new BorderLayout());
        pnlSouth.add(pnlLoadFile);
        pnlSouth.setVisible(false);
        this.add(pnlSouth, BorderLayout.SOUTH);

        fc = new JFileChooser();
    }

    private void bdyPerformed(Object src)
    {
        if (!(src instanceof JComboBox))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        JComboBox<String> cb = (JComboBox<String>) src;
        String bodyType = (String) cb.getSelectedItem();
        if (BodyType.STRING.getType().equals(bodyType))
        {
            pnlSouth.setVisible(false);
            txtAraBody.setEnabled(true);
            txtAraBody.setBackground(Color.white);
            txtAraBody.requestFocus();
        }
        else
        {
            pnlSouth.setVisible(true);
            txtFldPath.requestFocus();
            txtAraBody.setEnabled(false);
            txtAraBody.setBackground(UIUtil.lightGray());
        }
    }

    private void btnLoadPerformed(Object src)
    {
        if (!(src instanceof JButton))
        {
            return;
        }
        JButton btn = (JButton) src;
        if (!RESTConst.BROWSE.equals(btn.getName()))
        {
            return;
        }

        String content = UIUtil.openFile(this, fc);
        if (StringUtils.isEmpty(content))
        {
            return;
        }

        txtAraBody.setText(content);
    }

    private void menuPerformed(Object src)
    {
        if (!(src instanceof JMenuItem))
        {
            return;
        }

        JMenuItem item = (JMenuItem) (src);
        if (RESTConst.FORMAT.equals(item.getName()))
        {
            String body = RESTUtil.format(txtAraBody.getText());
            txtAraBody.setText(body);
            return;
        }

        if (RESTConst.COPY.equals(item.getName()))
        {
            StringSelection ss = null;
            String seltxt = txtAraBody.getSelectedText();
            if (StringUtils.isNotBlank(seltxt))
            {
                ss = new StringSelection(seltxt);
            }
            else
            {
                ss = new StringSelection(txtAraBody.getText());
            }

            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(ss, null);

            return;
        }

        if (RESTConst.PASTE.equals(item.getName()))
        {
            txtAraBody.paste();
            return;
        }

        if (RESTConst.CLEAR.equals(item.getName()))
        {
            txtAraBody.setText(StringUtils.EMPTY);
            return;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        this.bdyPerformed(e.getSource());
        this.btnLoadPerformed(e.getSource());
        this.menuPerformed(e.getSource());
    }

}
