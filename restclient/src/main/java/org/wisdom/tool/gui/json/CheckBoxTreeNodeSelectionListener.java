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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** 
* @Class Name : CheckBoxTreeNodeSelectionListener 
* @Description: Tree node selection listener for check box 
* @Author     : Dom Wang 
* @Email      : wisdomtool@qq.com
* @Date       : Feb 26, 2018 5:01:50 PM 
* @Version    : Wisdom RESTClient V1.2 
*/
public class CheckBoxTreeNodeSelectionListener extends MouseAdapter
{
    @Override
    public void mouseClicked(MouseEvent event)
    {
        JTree tree = (JTree) event.getSource();
        int x = event.getX();
        int y = event.getY();
        int row = tree.getRowForLocation(x, y);
        TreePath path = tree.getPathForRow(row);
        if (null == path)
        {
            return;
        }

        CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
        if (null == node)
        {
            return;
        }

        boolean selected = !node.isSelected();
        node.setSelected(selected);
        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
    }
}
