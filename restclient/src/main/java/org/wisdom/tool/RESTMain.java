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
package org.wisdom.tool;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.apidoc.APIUtil;
import org.wisdom.tool.cache.RESTCache;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.RESTView;
import org.wisdom.tool.gui.menu.MenuBarView;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.model.APIDoc;
import org.wisdom.tool.model.HttpHists;
import org.wisdom.tool.thread.LoadThd;
import org.wisdom.tool.util.RESTUtil;
import org.wisdom.tool.util.TestUtil;

/** 
 * @ClassName: RESTMain 
 * @Description: Rest Main
 * @Author: Yudong (Dom) Wang
 * @Email: wisdomtool@qq.com 
 * @Date: Jan 20, 2017 12:30:29 PM 
 * @Version: Wisdom RESTClient V1.2 
 */
public class RESTMain
{
    private static Logger log = LogManager.getLogger(RESTMain.class);

    private static WindowAdapter wa = new WindowAdapter()
    {
        public void windowClosing(WindowEvent e)
        {
            UIUtil.saveFile();
        }
    };

    /**
    * 
    * @Title      : load 
    * @Description: Load configurations through specified file 
    * @Param      : @param path 
    * @Return     : void
    * @Throws     :
     */
    public static void load(String path)
    {
        try
        {
            HttpHists hists = RESTUtil.loadHist(path);
            UIUtil.setRESTView(hists);
        }
        catch(Throwable e)
        {
            log.error("Failed to load file.", e);
        }
    }
    
    /**
    * 
    * @Title: init 
    * @Description: Component Initialization 
    * @param
    * @return void 
    * @throws
     */
    public static void init()
    {
        MenuBarView mbv = new MenuBarView();
        JFrame frame = new JFrame(RESTConst.REST_CLIENT_VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mbv.getJMenuBar());
        frame.getContentPane().add(RESTView.getView());
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(wa);
        frame.setIconImage(UIUtil.getImage(RESTConst.LOGO));
        frame.setMinimumSize(new Dimension(RESTConst.MAIN_FRAME_WIDTH, RESTConst.MAIN_FRAME_HEIGHT));
        UIUtil.setLocation(frame);
    }

    /**
    * 
    * @Title      : openView 
    * @Description: Open REST view 
    * @Param      : @param path open view through specified file
    * @Return     : void
    * @Throws     :
     */
    public static void openView(String path)
    {
        RESTCache.setCLIRunning(false);
        LoadThd loader = new LoadThd(path);
        loader.setName(RESTConst.LOAD_THREAD);
        SwingUtilities.invokeLater(loader);
    }

    /**
    * 
    * @Title: closeView 
    * @Description: Close REST view  
    * @param  
    * @return void
    * @throws
     */
    public static void closeView()
    {
        UIUtil.saveFile();
        System.exit(0);
    }

    /**
    * 
    * @Title      : launch 
    * @Description: - gui     -- launch GUI
    *             : - apitest -- test API
    *             : - apidoc  -- create API doc
    *             : - help    -- display help
    * @Param      : @param actions
    * @Return     : void
    * @Throws     :
     */
    public static void launch(String[] actions)
    {
        if (null == actions || actions.length < 1)
        {
            openView(RESTConst.EMPTY);
            return;
        }

        RESTUtil.closeSplashScreen();
        String path = RESTConst.EMPTY;
        if (actions.length > 1)
        {
            path = actions[1];
        }

        if (RESTConst.OPTION_HELP.equalsIgnoreCase(actions[0]))
        {
            RESTUtil.printUsage();
            System.exit(0);
        }
        else if (RESTConst.OPTION_DOC.equalsIgnoreCase(actions[0]))
        {
            RESTCache.setCLIRunning(true);
            APIDoc doc = APIUtil.loadDoc(path);
            APIUtil.apiDoc(doc);
            System.out.println(RESTConst.MSG_APIDOC);
            System.exit(0);
        }
        else if (RESTConst.OPTION_TEST.equalsIgnoreCase(actions[0]))
        {
            RESTCache.setCLIRunning(true);
            HttpHists hists = RESTUtil.loadHist(path);
            TestUtil.apiTest(hists);
            System.out.println(RESTConst.MSG_REPORT);
            System.exit(0);
        }
        else if (RESTConst.OPTION_GUI.equalsIgnoreCase(actions[0]))
        {
            openView(path);
        }
        else
        {
            RESTUtil.printUsage();
            System.exit(0);
        }
    }
    
    /**
    * 
    * @Title      : main 
    * @Description: REST Main
    *             : 
    * @Param      : @param args 
    * @Return     : void
    * @Throws     :
     */
    public static void main(String[] args)
    {        
        RESTMain.launch(args);
    }

}
