/*
 * Copyright 2018-present, Yudong (Dom) Wang
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

package org.wisdom.tool.gui.json;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wisdom.tool.constant.RESTConst;
import org.wisdom.tool.gui.util.UIUtil;
import org.wisdom.tool.model.HttpHist;
import org.wisdom.tool.util.RESTUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* @Class Name : JsonTree 
* @Description: Json tree view 
* @Author     : Dom Wang 
* @Email      : wisdomtool@qq.com
* @Date       : Feb 25, 2018 4:43:49 PM 
* @Version    : Wisdom RESTClient V1.2 
*/
public class JSONTree extends JPanel implements ActionListener
{
    private static Logger log = LogManager.getLogger(JSONTree.class);

    private static final long serialVersionUID = 7605731488849547300L;

    protected CheckBoxTreeNode rootNode;

    protected DefaultTreeModel treeModel;

    protected JTree tree = null;

    private HttpHist hist = null;

    private JPopupMenu pm = null;

    private JMenuItem miExpand = null;

    private JMenuItem miCollapse = null;

    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    private MouseAdapter ma = new CheckBoxTreeNodeSelectionListener()
    {
        private void popup(MouseEvent e)
        {
            int rc = tree.getRowCount();
            if (rc < 1)
            {
                miExpand.setEnabled(false);
                miCollapse.setEnabled(false);
                return;
            }
            else
            {
                miExpand.setEnabled(true);
                miCollapse.setEnabled(true);
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
    
    /**
    * 
    * @Title      : JSONTree 
    * @Description: Default constructor 
    * @Param      :
     */
    public JSONTree()
    {
        super(new GridLayout(1, 0));

        rootNode = new CheckBoxTreeNode(RESTConst.JSON);
        treeModel = new DefaultTreeModel(rootNode);

        tree = new JTree(treeModel);
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        tree.setEditable(false);
        tree.setToolTipText(RESTConst.EXCLUDE_NODE);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(ma);

        JScrollPane scrollPane = new JScrollPane(tree);
        this.add(scrollPane);
        this.initPopupMenu();
    }

    /**
    * 
    * @Title      : initPopupMenu 
    * @Description: PopupMenu Initialization 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public void initPopupMenu()
    {
        pm = new JPopupMenu();

        miExpand = new JMenuItem(RESTConst.EXPAND_ALL);
        miExpand.setName(RESTConst.EXPAND_ALL);
        miExpand.addActionListener(this);

        miCollapse = new JMenuItem(RESTConst.COLLAPSE_ALL);
        miCollapse.setName(RESTConst.COLLAPSE_ALL);
        miCollapse.addActionListener(this);

        pm.add(miExpand);
        pm.addSeparator();
        pm.add(miCollapse);
    }
    
    /**
    * 
    * @Title      : clear 
    * @Description: Remove all nodes except the root node. 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public void clear()
    {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /**
    * 
    * @Title      : removeCurrentNode 
    * @Description: Remove the currently selected node. 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public void removeCurrentNode()
    {
        TreePath currentSelection = tree.getSelectionPath();
        if (null == currentSelection)
        {
            return;
        }

        CheckBoxTreeNode currentNode = (CheckBoxTreeNode) (currentSelection.getLastPathComponent());
        MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
        if (null != parent)
        {
            treeModel.removeNodeFromParent(currentNode);
            return;
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /**
    * 
    * @Title      : addObject 
    * @Description: Add child to the currently selected node. 
    * @Param      : @param child
    * @Param      : @return 
    * @Return     : CheckBoxTreeNode
    * @Throws     :
     */
    public CheckBoxTreeNode addObject(Object child)
    {
        CheckBoxTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (null == parentPath)
        {
            parentNode = rootNode;
        }
        else
        {
            parentNode = (CheckBoxTreeNode) (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    /**
    * 
    * @Title      : addObject 
    * @Description: Add child to the parent node. 
    * @Param      : @param parent
    * @Param      : @param child
    * @Param      : @return 
    * @Return     : CheckBoxTreeNode
    * @Throws     :
     */
    public CheckBoxTreeNode addObject(CheckBoxTreeNode parent, Object child)
    {
        return addObject(parent, child, false);
    }

    /**
    * 
    * @Title      : addObject 
    * @Description: Add child to the parent node, 
    *               and make sure the new node is visible. 
    * @Param      : @param parent
    * @Param      : @param child
    * @Param      : @param shouldBeVisible
    * @Param      : @return 
    * @Return     : CheckBoxTreeNode
    * @Throws     :
     */
    public CheckBoxTreeNode addObject(CheckBoxTreeNode parent, Object child, boolean shouldBeVisible)
    {
        CheckBoxTreeNode childNode = new CheckBoxTreeNode(child);
        if (null == parent)
        {
            parent = rootNode;
        }

        // It is key to invoke this on the TreeModel
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible)
        {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /**
    * 
    * @Title      : getRootNode 
    * @Description: Get root node 
    * @Param      : @return 
    * @Return     : CheckBoxTreeNode
    * @Throws     :
     */
    public CheckBoxTreeNode getRootNode()
    {
        return rootNode;
    }

    public DefaultTreeModel getTreeModel()
    {
        return treeModel;
    }

    public JTree getTree()
    {
        return tree;
    }

    public HttpHist getHist()
    {
        return hist;
    }

    public void setHist(HttpHist hist)
    {
        this.hist = hist;
    }

    /**
    * 
    * @Title      : jsonTree 
    * @Description: Generate JSON tree 
    * @Param      : @param jnode
    * @Param      : @param tree
    * @Param      : @param pnode 
    * @Return     : void
    * @Throws     :
     */
    private void jsonTree(JsonNode jnode, CheckBoxTreeNode pnode, String path)
    {
        if (jnode.isValueNode())
        {
            return;
        }

        if (jnode.isObject())
        {
            Iterator<Entry<String, JsonNode>> it = jnode.fields();
            while (it.hasNext())
            {
                Entry<String, JsonNode> en = it.next();
                String pname = new String(en.getKey());
                CheckBoxTreeNode node = this.addObject(pnode, pname);

                String nodeKey = path + "|" + en.getKey();
                if (CollectionUtils.isNotEmpty(hist.getExcludedNodes()))
                {
                    if (hist.getExcludedNodes().contains(nodeKey))
                    {
                        node.setSelected(true);
                    }
                }

                jsonTree(en.getValue(), node, nodeKey);
            }
        }

        if (jnode.isArray())
        {
            Iterator<JsonNode> it = jnode.iterator();
            if (it.hasNext())
            {
                jsonTree(it.next(), pnode, path);
            }
        }
    }
    
    /**
    * 
    * @Title      : populateTree 
    * @Description: populate tree view 
    * @Param      :  
    * @Return     : void
    * @Throws     :
     */
    public void populateTree()
    {
        String json = hist.getRsp().getBody();
        this.populateTree(json);
    }
    
    /**
    * 
    * @Title      : populateTree 
    * @Description: populate tree 
    * @Param      : @param json 
    * @Return     : void
    * @Throws     :
     */
    public void populateTree(String json)
    {
        if (!RESTUtil.isJson(json))
        {
            return;
        }

        ObjectMapper om = new ObjectMapper();

        try
        {
            this.clear();
            JsonNode node = om.readTree(json);
            jsonTree(node, null, "");
        }
        catch(Exception e)
        {
            log.error("Failed to read JSON tree.", e);
        }
    }

    /**
    * 
    * @Title      : selectedNodes 
    * @Description: Selected nodes 
    * @Param      : @param node
    * @Param      : @param selectedNodes 
    * @Return     : void
    * @Throws     :
     */
    private void selectedNodes(CheckBoxTreeNode node, List<String> selectedNodes)
    {
        if (node.isSelected())
        {
            String nodeKey = RESTConst.EMPTY;
            Object[] names = node.getUserObjectPath();
            for (int i = 1; i < names.length; i++)
            {
                nodeKey += "|" + names[i];
            }
            if (StringUtils.isNotEmpty(nodeKey))
            {
                selectedNodes.add(nodeKey);
            }
        }

        if (node.isLeaf())
        {
            return;
        }

        Enumeration<?> children = node.children();
        while (children.hasMoreElements())
        {
            CheckBoxTreeNode child = (CheckBoxTreeNode) children.nextElement();
            selectedNodes(child, selectedNodes);
        }
    }

    /**
    * 
    * @Title      : getSelectedNodes 
    * @Description: Get selected nodes  
    * @Param      : @return 
    * @Return     : List<String>
    * @Throws     :
     */
    public List<String> getSelectedNodes()
    {
        List<String> selectedNodes = new ArrayList<String>();
        this.selectedNodes(this.getRootNode(), selectedNodes);
        return selectedNodes;
    }

    public void actionPerformed(ActionEvent e)
    {
        JMenuItem item = (JMenuItem) (e.getSource());
        if (RESTConst.EXPAND_ALL.equals(item.getName()))
        {
            UIUtil.expand(this.tree);
            return;
        }

        if (RESTConst.COLLAPSE_ALL.equals(item.getName()))
        {
            UIUtil.collapse(this.tree);
            return;
        }
    }

}
