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

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.RESTMain;
import org.wisdom.tool.apidoc.APIUtil;
import org.wisdom.tool.cache.RESTCache;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.RESTView;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.model.APIDoc;
import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.thread.RESTThdPool;
import org.wisdom.tool.thread.TestThd;
import org.wisdom.tool.util.RESTUtil;
import org.wisdom.tool.util.TestUtil;

/** 
 * @ClassName: MenuBarView 
 * @Description: Menu bar view
 * @Author: Yudong (Dom) Wang
 * @Email: wisdomtool@qq.com 
 * @Date: Jan 20, 2017 12:30:29 PM 
 * @Version: Wisdom RESTClient V1.2 
 */
public class MenuBarView implements ActionListener, PropertyChangeListener
{
    private static Logger log = LogManager.getLogger(MenuBarView.class);

    private JMenuBar mb = null;

    private JFileChooser fc = null;

    private ProgressMonitor pm = null;

    private JMenuItem miStart = null;

    private JMenuItem miStop = null;

    private HistTask task = null;

    private TestThd testThrd = null;

    private DonateDialog dd = null;

    private AboutDialog ad = null;

    class HistTask extends SwingWorker<Void, Void>
    {
        private HttpHists hists = null;

        public HistTask(HttpHists hists)
        {
            super();
            this.hists = hists;
        }

        @Override
        public Void doInBackground()
        {
            int done = 0;
            int progress = 0;
            this.setProgress(0);
            while (progress < hists.getTotal() && !isCancelled())
            {
                progress = hists.progress();
                done = Math.min(progress, hists.getTotal()) * 100 / hists.getTotal();
                this.setProgress(done);
                RESTUtil.sleep(RESTConst.TIME_100MS);
            }
            return null;
        }

        @Override
        public void done()
        {
            Toolkit.getDefaultToolkit().beep();
            miStart.setEnabled(true);
            pm.setProgress(0);
            pm.close();
        }
    }

    public MenuBarView()
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
        JMenu mnFile = new JMenu(RESTConst.FILE);
        JMenu mnEdit = new JMenu(RESTConst.EDIT);
        JMenu mnTest = new JMenu(RESTConst.TEST);
        JMenu mnDoc = new JMenu(StringUtils.capitalize(RESTConst.APIDOC));
        //JMenu mnTool = new JMenu(RESTConst.TOOLS);
        JMenu mnHelp = new JMenu(RESTConst.HELP);

        // Menu of file
        JMenuItem miImport = new JMenuItem(RESTConst.IMPORT);
        JMenuItem miExport = new JMenuItem(RESTConst.EXPORT);
        JMenuItem miExit = new JMenuItem(RESTConst.EXIT);

        miImport.addActionListener(this);
        miImport.setToolTipText(RESTConst.IMPORT + " " + RESTConst.HIST);
        
        miExport.addActionListener(this);
        miExport.setToolTipText(RESTConst.EXPORT + " " + RESTConst.HIST);
        miExit.addActionListener(this);

        mnFile.add(miImport);
        mnFile.add(miExport);
        mnFile.addSeparator();
        mnFile.add(miExit);
        
        // Menu of edit
        JMenuItem miResetReq = new JMenuItem(RESTConst.RESET_REQ);
        JMenuItem miResetRsp = new JMenuItem(RESTConst.RESET_RSP);
        JMenuItem miResetAll = new JMenuItem(RESTConst.RESET_ALL);
        JMenuItem miRmHist = new JMenuItem(RESTConst.RM_ALL);

        miResetReq.addActionListener(this);
        miResetRsp.addActionListener(this);
        
        miResetAll.addActionListener(this);
        miResetAll.setToolTipText(RESTConst.RESET + " " + RESTConst.REQUEST + " & " + RESTConst.RESPONSE);

        miRmHist.addActionListener(this);
        miRmHist.setToolTipText(RESTConst.RM_ALL + " " + RESTConst.HIST);

        mnEdit.add(miResetReq);
        mnEdit.add(miResetRsp);
        mnEdit.add(miResetAll);
        mnEdit.addSeparator();
        mnEdit.add(miRmHist);

        // Menu of test
        miStart = new JMenuItem(RESTConst.START_TEST);
        miStop = new JMenuItem(RESTConst.STOP_TEST);
        JMenuItem miReport = new JMenuItem(RESTConst.TEST_REPORT);

        miStart.setToolTipText(RESTConst.START_TEST + " " + RESTConst.HIST);
        miStart.addActionListener(this);

        miStop.setToolTipText(RESTConst.STOP_TEST + " " + RESTConst.HIST);
        miStop.addActionListener(this);
        miStop.setEnabled(false);

        miReport.setToolTipText(RESTConst.DISPLAY + " " + RESTConst.TEST_REPORT);
        miReport.addActionListener(this);

        mnTest.add(miStart);
        mnTest.add(miStop);
        mnTest.addSeparator();
        mnTest.add(miReport);

        // Menu of API DOC
        JMenuItem miCreate = new JMenuItem(RESTConst.CREATE);
        JMenuItem miOpen = new JMenuItem(RESTConst.OPEN);
        
        miCreate.setToolTipText(RESTConst.CREATE + " " + RESTConst.API_DOCUMENT);
        miCreate.addActionListener(this);
        
        miOpen.setToolTipText(RESTConst.OPEN + " " + RESTConst.API_DOCUMENT);
        miOpen.addActionListener(this);
        
        mnDoc.add(miCreate);
        mnDoc.addSeparator();
        mnDoc.add(miOpen);
        
        // Menu of help
        JMenuItem miContent = new JMenuItem(RESTConst.HELP_CONTENTS);
        JMenuItem miIssue = new JMenuItem(RESTConst.REPORT_ISSUE);
        JMenuItem miDonate = new JMenuItem(RESTConst.DONATE);
        JMenuItem miAbout = new JMenuItem(RESTConst.ABOUT_TOOL);

        miContent.addActionListener(this);
        miIssue.addActionListener(this);
        miDonate.addActionListener(this);
        miAbout.addActionListener(this);

        mnHelp.add(miContent);
        mnHelp.add(miIssue);
        mnHelp.addSeparator();
        mnHelp.add(miDonate);
        mnHelp.addSeparator();
        mnHelp.add(miAbout);

        ad = new AboutDialog();
        dd = new DonateDialog();

        // MenuBar
        mb = new JMenuBar();
        mb.setBorder(BorderFactory.createEtchedBorder());
        mb.add(mnFile);
        mb.add(mnEdit);
        mb.add(mnTest);
        mb.add(mnDoc);
        // mb.add(mnTool);
        mb.add(mnHelp);
        fc = new JFileChooser();
    }

    public JMenuBar getJMenuBar()
    {
        return mb;
    }

    /**
    * 
    * @Title: filePerformed 
    * @Description: File Menu Item Performed
    * @param @param item
    * @return void 
    * @throws
     */
    private void filePerformed(JMenuItem item)
    {
        if (RESTConst.IMPORT.equals(item.getText()))
        {
            String content = UIUtil.openFile(RESTView.getView(), fc);
            HttpHists hists = RESTUtil.toOject(content, HttpHists.class);
            UIUtil.setRESTView(hists);
            return;
        }

        if (RESTConst.EXPORT.equals(item.getText()))
        {
            UIUtil.saveFile(RESTView.getView(), fc);
            return;
        }

        if (RESTConst.EXIT.equals(item.getText()))
        {
            RESTMain.closeView();
            return;
        }
    }
    
    /**
    * 
    * @Title: editPerformed 
    * @Description: Edit Menu Item Performed 
    * @param @param item 
    * @return void 
    * @throws
     */
    private void editPerformed(JMenuItem item)
    {
        if (RESTConst.RESET_REQ.equals(item.getText()))
        {
            RESTView.getView().getReqView().reset();
            return;
        }

        if (RESTConst.RESET_RSP.equals(item.getText()))
        {
            RESTView.getView().getRspView().reset();
            return;
        }

        if (RESTConst.RESET_ALL.equals(item.getText()))
        {
            RESTView.getView().getReqView().reset();
            RESTView.getView().getRspView().reset();
            return;
        }

        if (RESTConst.RM_ALL.equals(item.getText()))
        {
            JOptionPane.setDefaultLocale(Locale.US);
            int ret = JOptionPane.showConfirmDialog(RESTView.getView(), 
                                                    RESTConst.CONFIRM_RM_ALL, 
                                                    RESTConst.RM_ALL, 
                                                    JOptionPane.YES_NO_OPTION);
            if (0 == ret)
            {
                RESTCache.getHists().clear();
                RESTView.getView().getHistView().getTabMdl().clear();
            }
            return;
        }
    }
    
    /**
    * 
    * @Title: testPerformed 
    * @Description: Test Menu Item Performed 
    * @param @param item
    * @return void
    * @throws
     */
    private void testPerformed(JMenuItem item)
    {
        if (RESTConst.START_TEST.equals(item.getText()))
        {
            if (MapUtils.isEmpty(RESTCache.getHists()))
            {
                return;
            }

            miStart.setEnabled(false);
            miStop.setEnabled(true);

            HttpHists hists = new HttpHists(RESTCache.getHists().values());

            pm = new ProgressMonitor(RESTView.getView(), RESTConst.TEST_CASE, "", 0, hists.getTotal());
            pm.setProgress(0);

            task = new HistTask(hists);
            task.addPropertyChangeListener(this);
            task.execute();

            testThrd = new TestThd(hists);
            testThrd.setName(RESTConst.TEST_THREAD);
            RESTThdPool.getInstance().getPool().submit(testThrd);
        }

        if (RESTConst.STOP_TEST.equals(item.getText()))
        {
            if (null == testThrd)
            {
                return;
            }

            try
            {
                miStop.setEnabled(false);

                pm.close();
                task.cancel(true);

                testThrd.getHists().setStop(true);
                testThrd.interrupt();

                miStart.setEnabled(true);
            }
            catch(Exception ex)
            {
                log.error("Failed to interrupt test thread.", ex);
            }
        }

        if (RESTConst.TEST_REPORT.equals(item.getText()))
        {
            TestUtil.open(RESTConst.REPORT_HTML, 
                          RESTConst.MSG_REPORT, 
                          RESTConst.TEST_REPORT);
        }

    }

    /**
    * 
    * @Title: apiDocPerformed 
    * @Description: API DOC Menu Item Performed 
    * @param @param item 
    * @return void 
    * @throws
     */
    private void apiDocPerformed(JMenuItem item)
    {
        if (RESTConst.CREATE.equals(item.getText()))
        {
            APIDoc doc = APIUtil.getAPIDoc();
            APIUtil.apiDoc(doc);
            return;
        }

        if (RESTConst.OPEN.equals(item.getText()))
        {
            TestUtil.open(RESTConst.APIDOC_HTML, 
                          RESTConst.MSG_APIDOC, 
                          RESTConst.API_DOCUMENT);
        }
    }
    
    /**
    * 
    * @Title: helpPerformed 
    * @Description: Help Menu Item Performed 
    * @param @param item
    * @return void
    * @throws
     */
    private void helpPerformed(JMenuItem item)
    {
        if (RESTConst.ABOUT_TOOL.equals(item.getText()))
        {
            ad.setVisible(true);
            UIUtil.setLocation(ad);
            return;
        }

        if (RESTConst.DONATE.equals(item.getText()))
        {
            dd.setVisible(true);
            UIUtil.setLocation(dd);
            return;
        }

        if (RESTConst.HELP_CONTENTS.equals(item.getText()))
        {
            try
            {
                String path = RESTUtil.replacePath(RESTConst.HELP_DOC);
                InputStream is = RESTUtil.getInputStream(RESTConst.HELP_DOC);
                if (null == is)
                {
                    return;
                }
                FileUtils.copyInputStreamToFile(is, new File(path));
                RESTUtil.close(is);
                try
                {
                    Desktop.getDesktop().open(new File(path));
                }
                catch(Exception e)
                {
                    UIUtil.showMessage(RESTConst.MSG_HELP_FILE, RESTConst.HELP_CONTENTS);
                }
            }
            catch(IOException e)
            {
                log.error("Failed to open help document.", e);
            }
            return;
        }

        if (RESTConst.REPORT_ISSUE.equals(item.getText()))
        {
            try
            {
                Desktop.getDesktop().browse(new URI(RESTConst.URL_ISSUE));
            }
            catch(Exception e)
            {
                UIUtil.showMessage(RESTConst.MSG_REPORT_ISSUE, RESTConst.REPORT_ISSUE);;
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        JMenuItem item = (JMenuItem) (e.getSource());
        this.filePerformed(item);
        this.editPerformed(item);
        this.testPerformed(item);
        this.apiDocPerformed(item);
        this.helpPerformed(item);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (!RESTConst.PROGRESS.equals(evt.getPropertyName()))
        {
            return;
        }

        int progress = (Integer) evt.getNewValue();
        pm.setProgress(progress);

        String message = String.format("Completed %d%%.\n", progress * 100 / pm.getMaximum());
        pm.setNote(message);

        if (pm.isCanceled() || task.isDone())
        {
            Toolkit.getDefaultToolkit().beep();
            if (pm.isCanceled())
            {
                task.cancel(true);
                testThrd.getHists().setStop(true);
                testThrd.interrupt();
            }
            miStart.setEnabled(true);
            miStop.setEnabled(false);
        }
    }
}
