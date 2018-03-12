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
package org.wisdom.tool.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.cache.RESTCache;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.RESTView;
import org.wisdom.tool.gui.common.TabModel;
import org.wisdom.tool.gui.req.ReqView;
import org.wisdom.tool.model.BodyType;
import org.wisdom.tool.model.Charsets;
import org.wisdom.tool.model.HttpHist;
import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.model.HttpMethod;
import org.wisdom.tool.model.HttpReq;
import org.wisdom.tool.model.HttpRsp;
import org.wisdom.tool.util.RESTClient;
import org.wisdom.tool.util.RESTUtil;

/** 
* @ClassName: UIUtil 
* @Description: UI Utility 
* @Author: Yudong (Dom) Wang
* @Email: wisdomtool@outlook.com
* @Date: July 20, 2017 12:30:29 PM 
* @Version: Wisdom RESTClient V1.2 
*/
public class UIUtil
{
    private static Logger log = LogManager.getLogger(UIUtil.class);
    
    /**
    * 
    * @Title: getIcon
    * @Description: Get icon from class path 
    * @param @param path
    * @param @return
    * @return ImageIcon
    * @throws
     */
    public static ImageIcon getIcon(String path)
    {
        URL url = UIUtil.class.getClassLoader().getResource(path);
        return new ImageIcon(url);
    }
    
    /**
    * 
    * @Title: getImage 
    * @Description: Get image from class path
    * @param @param path
    * @param @return
    * @return Image 
    * @throws
     */
    public static Image getImage(String path)
    {
        URL url = UIUtil.class.getClassLoader().getResource(path);
        return Toolkit.getDefaultToolkit().getImage(url);
    }
    
    /**
    * 
    * @Title: getValuePair 
    * @Description: get key/value pair 
    * @param @param values
    * @param @return 
    * @return Map<String,String>
    * @throws
     */
    public static Map<String, String> getValuePair(Collection<List<Object>> values)
    {
        Map<String, String> valMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isEmpty(values))
        {
            return valMap;
        }

        String key = StringUtils.EMPTY;
        String val = StringUtils.EMPTY;
        for (List<Object> valLst : values)
        {
            if (valLst.size() < 1)
            {
                continue;
            }

            key = String.valueOf(valLst.get(0));
            if (valLst.size() > 1)
            {
                val = String.valueOf(valLst.get(1));
            }
            valMap.put(key, val);
        }

        return valMap;
    }

    /**
    * 
    * @Title: lightGray 
    * @Description: light gray color
    * @param @return 
    * @return Color
    * @throws
     */
    public static Color lightGray()
    {
        return new Color(RESTConst.LIGHT_GRAY, RESTConst.LIGHT_GRAY, RESTConst.LIGHT_GRAY);
    }
    
    /**
    * 
    * @Title: openFile 
    * @Description: open file to get file content 
    * @param @param parent
    * @param @param fc
    * @param @return 
    * @return String
    * @throws
     */
    public static String openFile(Component parent, JFileChooser fc)
    {
        String content = StringUtils.EMPTY;
        int retVal = fc.showOpenDialog(parent);
        if (JFileChooser.APPROVE_OPTION != retVal)
        {
            return content;
        }

        try
        {
            File sf = fc.getSelectedFile();
            content = FileUtils.readFileToString(sf, Charsets.UTF_8.getCname());
        }
        catch(IOException e)
        {
            log.error("Failed to read file.", e);
        }

        return content;
    }
    
    /**
    * 
    * @Title: saveFile 
    * @Description: Save HTTP history to file 
    * @param @param parent
    * @param @param fc 
    * @return void
    * @throws
     */
    public static void saveFile(Component parent, JFileChooser fc)
    {
        int retVal = fc.showSaveDialog(parent);
        if (JFileChooser.APPROVE_OPTION != retVal)
        {
            return;
        }

        File fhist = fc.getSelectedFile();
        List<HttpHist> histLst = new ArrayList<HttpHist>(RESTCache.getHists().values());
        HttpHists hists = new HttpHists(histLst);
        RESTUtil.toJsonFile(fhist, hists);
    }
    
    /**
    * 
    * @Title: saveFile 
    * @Description: Save HTTP history to file  
    * @param  
    * @return void
    * @throws
     */
    public static void saveFile()
    {
        File fhist = new File(RESTConst.HTTP_HIST_JSON);
        try
        {
            if (!fhist.exists())
            {
                FileUtils.forceMkdirParent(fhist);
                fhist.createNewFile();
            }
        }
        catch(IOException ie)
        {
            log.error("Failed to create new file.", ie);
            return;
        }

        List<HttpHist> histLst = new ArrayList<HttpHist>(RESTCache.getHists().values());
        HttpHists hists = new HttpHists(histLst);
        RESTUtil.toJsonFile(fhist, hists);
    }
    
    /**
    * 
    * @Title: setRESTView 
    * @Description: To set REST view 
    * @param @param hists 
    * @return void
    * @throws
     */
    public static void setRESTView(HttpHists hists)
    {
        if (null == hists || CollectionUtils.isEmpty(hists.getHists()))
        {
            return;
        }

        // Clear old data
        RESTView.getView().getReqView().reset();
        RESTView.getView().getRspView().reset();
        RESTView.getView().getHistView().getTabMdl().clear();
        RESTCache.getHists().clear();

        // Set with new data
        List<HttpHist> histLst = hists.getHists();
        for (HttpHist h : histLst)
        {
            RESTView.getView().getHistView().setHistView(h);
        }

        HttpHist lastHist = histLst.get(histLst.size() - 1);
        RESTView.getView().getReqView().setReqView(lastHist.getReq());
        RESTView.getView().getRspView().setRspView(lastHist.getRsp());
    }
    
    /**
    * 
    * @Title: contents 
    * @Description: get file contents
    * @param @return
    * @return String 
    * @throws
     */
    public static String contents(String filename)
    {
        String content = StringUtils.EMPTY;
        try
        {
            InputStream is = RESTUtil.getInputStream(filename);
            content = IOUtils.toString(is, Charsets.UTF_8.getCname());
            RESTUtil.close(is);
        }
        catch(IOException e)
        {
            log.error("Failed to read file.", e);
        }
        return content;
    }

    /**
    * 
    * @Title: submit 
    * @Description: Submit HTTP request 
    * @param  
    * @return void
    * @throws
     */
    public static void submit(ReqView rv)
    {
        String url = (String) rv.getCbUrl().getSelectedItem();
        if (StringUtils.isBlank(url))
        {
            return;
        }

        HttpMethod method = (HttpMethod) rv.getCbMtd().getSelectedItem();
        String btype = (String) rv.getPnlBody().getCbBodyType().getSelectedItem();
        String charset = (String) rv.getPnlBody().getCbCharset().getSelectedItem();
        String ctype = (String) rv.getPnlBody().getCbContentType().getSelectedItem();
        String body = rv.getPnlBody().getTxtAraBody().getText();
        String path = rv.getPnlBody().getTxtFldPath().getText();

        try
        {
            if (BodyType.FILE.getType().equals(btype))
            {
                File f = new File(path);
                if (f.exists())
                {
                    body = FileUtils.readFileToString(new File(path), charset);
                }
            }
        }
        catch(IOException e)
        {
            log.error("Failed to read file.", e);
        }

        Map<String, String> headers = UIUtil.getValuePair(rv.getPnlHdr().getTabMdl().getValues());
        Map<String, String> cookies = UIUtil.getValuePair(rv.getPnlCookie().getTabMdl().getValues());
        headers.put(RESTConst.CONTENT_TYPE, ctype + "; charset=" + charset);
        if (null == headers.get(RESTConst.ACCEPT))
        {
            headers.put(RESTConst.ACCEPT, RESTConst.ACCEPT_TYPE);
        }

        HttpReq req = new HttpReq(method, url, body, headers, cookies);
        HttpRsp rsp = RESTClient.getInstance().exec(req);

        RESTView.getView().getRspView().setRspView(rsp);
        RESTView.getView().getHistView().setHistView(req, rsp);
    }

    /**
    * 
    * @Title: setLocation 
    * @Description: set component's location
    * @param @param c 
    * @return void
    * @throws
     */
    public static void setLocation(Component c)
    {
        int winWidth = c.getWidth();
        int winHeight = c.getHeight();

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        c.setLocation(screenWidth / 2 - winWidth / 2, screenHeight / 2 - winHeight / 2);
    }

    /**
    * @Title      : setHistTabWidth 
    * @Description: set history table width 
    * @Param      : @param tab 
    * @Return     : void
    * @Throws     :
     */
    public static void setHistTabWidth(JTable tab)
    {
        int width[] = { 35, 260, 81, 144, 50, 80 };
        TableColumnModel cols = tab.getColumnModel();
        for (int i = 0; i < width.length; i++)
        {
            if (width[i] < 0)
            {
                continue;
            }

            TableColumn col = cols.getColumn(i);
            if (i == 1 || i == 4 || i == 5)
            {
                col.setMinWidth(width[i]);
                continue;
            }

            col.setMinWidth(width[i]);
            col.setMaxWidth(width[i]);
        }
    }
    
    /**
    * @Title      : rmRows 
    * @Description: Remove selected rows 
    * @Param      : @param tab
    * @Param      : @param tabMdl 
    * @Return     : void
    * @Throws     :
     */
    public static void rmRows(JTable tab, TabModel tabMdl)
    {
        int src = tab.getSelectedRowCount();
        if (src < 1)
        {
            return;
        }

        String key = StringUtils.EMPTY;
        int[] rows = tab.getSelectedRows();
        for (int i : rows)
        {
            key = tabMdl.getRowKey(i);
            RESTCache.getHists().remove(key);
        }
        tabMdl.deleteRow(rows);

        int rc = tabMdl.getRowCount();
        for (int i = 0; i < rc; i++)
        {
            tabMdl.setValueAt(i + 1, i, 0);
        }
    }

    /**
    * 
    * @Title      : updateCache 
    * @Description: Update cache set assertbody and description field 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public static void updateCache()
    {
        if (MapUtils.isEmpty(RESTCache.getHists()))
        {
            return;
        }

        Object dscr = null;
        Map<String, Object> dscrCols = RESTView.getView().getHistView().getTabMdl().getColumn(5);
        for (HttpHist h : RESTCache.getHists().values())
        {
            // Update description field
            dscr = dscrCols.get(h.getKey());
            if (null != dscr)
            {
                h.setDescr(String.valueOf(dscr));
            }
        }
    }
    
    /**
    * @Title      : mvup 
    * @Description: Move up 
    * @Param      : @param es
    * @Param      : @param row 
    * @Return     : void
    * @Throws     :
     */
    private static void mvup(List<Entry<String, HttpHist>> es, int[] row)
    {
        Entry<String, HttpHist> ep = null;
        Entry<String, HttpHist> ec = null;

        for (int i = 0; i < row.length; i++)
        {
            if (row[i] <= 0)
            {
                continue;
            }

            ep = es.get(row[i] - 1);
            ec = es.get(row[i]);

            es.set(row[i] - 1, ec);
            es.set(row[i], ep);
        }
    }
    
    /**
    * @Title      : mvdown 
    * @Description: Move down 
    * @Param      : @param es
    * @Param      : @param row 
    * @Return     : void
    * @Throws     :
     */
    private static void mvdown(List<Entry<String, HttpHist>> es, int[] row)
    {
        Entry<String, HttpHist> ec = null;
        Entry<String, HttpHist> en = null;

        for (int i = row.length - 1; i >= 0; i--)
        {
            if (row[i] >= es.size() - 1)
            {
                continue;
            }

            ec = es.get(row[i]);
            en = es.get(row[i] + 1);

            es.set(row[i], en);
            es.set(row[i] + 1, ec);
        }
    }
    
    /**
    * @Title      : move 
    * @Description: Move up or down rows 
    * @Param      : @param tab
    * @Param      : @param tabMdl 
    * @Return     : void
    * @Throws     :
     */
    public static void move(JTable tab, TabModel tabMdl, boolean isup)
    {
        int src = tab.getSelectedRowCount();
        if (src < 1)
        {
            return;
        }

        HttpReq req = null;
        HttpRsp rsp = null;
        
        updateCache();
        
        List<Entry<String, HttpHist>> es = new ArrayList<Entry<String,HttpHist>>(RESTCache.getHists().entrySet());
        int[] row = tab.getSelectedRows();
        if (isup) // Move up
        {
            mvup(es, row);
        }
        else // Move down
        {
            mvdown(es, row);
        }

        RESTCache.getHists().clear();
        tabMdl.clear();

        int i = 1;
        String key = StringUtils.EMPTY;
        for (Entry<String, HttpHist> e : es)
        {
            req = e.getValue().getReq();
            rsp = e.getValue().getRsp();

            key = tabMdl.insertRow(i, 
                         req.getMethod() + " " + req.getUrl(),
                         rsp.getStatus(), 
                         rsp.getDate(), 
                         rsp.getTime() + "ms",
                         e.getValue().getDescr());

            RESTCache.getHists().put(key, e.getValue());
            i++;
        }
    }
    
    /**
    * @Title      : showMessage 
    * @Description: If it is not CLI running, 
    *             : require a message dialog to tell user where is the file.
    * @Param      : @param msg
    * @Param      : @param title 
    * @Return     : void
    * @Throws     :
     */
    public static void showMessage(final String msg, final String title)
    {
        if (!RESTCache.isCLIRunning())
        {
            JOptionPane.showMessageDialog(RESTView.getView(), 
                                          msg, 
                                          title, 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
    * 
    * @Title      : getSelectedHist 
    * @Description: Get first selected history case 
    * @Param      : @param tab
    * @Param      : @param tabMdl
    * @Param      : @return 
    * @Return     : String
    * @Throws     :
     */
    public static HttpHist getSelectedHist(JTable tab, TabModel tabMdl)
    {
        HttpHist hist = null;
        int row = tab.getSelectedRow();
        if (row < 0)
        {
            return hist;
        }

        String key = tabMdl.getRowKey(row);
        hist = RESTCache.getHists().get(key);
        return hist;
    }

    /**
    * 
    * @Title      : setSelectedHist 
    * @Description: Set first selected history case  
    * @Param      : @param hist
    * @Param      : @param tab
    * @Param      : @param tabMdl 
    * @Return     : void
    * @Throws     :
     */
    public static void setSelectedHist(HttpHist hist, JTable tab, TabModel tabMdl)
    {
        if (null == hist)
        {
            return;
        }

        int row = tab.getSelectedRow();
        if (row < 0)
        {
            return;
        }

        List<Object> rowData = tabMdl.getRow(row);
        List<Object> values = hist.toRow(rowData.get(0));
        tabMdl.setRowValues(values, row);
    }

    /**
    * 
    * @Title      : refreshHistView 
    * @Description: TODO 
    * @Param      : @param hs
    * @Param      : @param tabMdl 
    * @Return     : void
    * @Throws     :
     */
    public static void refreshHistView(Collection<HttpHist> hs, TabModel tabMdl)
    {
        if (CollectionUtils.isEmpty(hs))
        {
            return;
        }

        List<HttpHist> hists = new ArrayList<HttpHist>(hs);
        for (int row = 0; row < hists.size(); row++)
        {
            List<Object> rowData = tabMdl.getRow(row);
            if (CollectionUtils.isEmpty(rowData))
            {
                continue;
            }
            HttpHist hist = hists.get(row);
            List<Object> values = hist.toRow(rowData.get(0));
            tabMdl.setRowValues(values, row);
        }
    }
    
    /**
    * 
    * @Title      : expand 
    * @Description: Expand tree nodes
    * @Param      : @param tree 
    * @Return     : void
    * @Throws     :
     */
    public static void expand(JTree tree)
    {
        for (int r = 0; r < tree.getRowCount(); r++)
        {
            tree.expandRow(r);
        }
    }

    /**
    * 
    * @Title      : collapse 
    * @Description: Collapse tree nodes
    * @Param      : @param tree 
    * @Return     : void
    * @Throws     :
     */
    public static void collapse(JTree tree)
    {
        for (int r = 0; r < tree.getRowCount(); r++)
        {
            tree.collapseRow(r);
        }
    }
}
