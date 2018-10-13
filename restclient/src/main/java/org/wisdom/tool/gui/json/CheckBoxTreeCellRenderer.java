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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

/** 
* @Class Name : CheckBoxTreeCellRenderer 
* @Description: Tree cell renderer for check box 
* @Author     : Dom Wang 
* @Email      : wisdomtool@qq.com
* @Date       : Feb 26, 2018 4:54:49 PM 
* @Version    : Wisdom RESTClient V1.2 
*/
public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    private static final long serialVersionUID = -4024170383791509257L;

    protected JCheckBox check;

    protected CheckBoxTreeLabel label;

    public CheckBoxTreeCellRenderer()
    {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new CheckBoxTreeLabel());
        check.setBackground(UIManager.getColor("Tree.textBackground"));
        label.setForeground(UIManager.getColor("Tree.textForeground"));
    }

    public Component getTreeCellRendererComponent(JTree tree, 
                                                  Object value,
                                                  boolean selected, 
                                                  boolean expanded, 
                                                  boolean leaf, 
                                                  int row,
                                                  boolean hasFocus)
    {
        String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());

        check.setSelected(((CheckBoxTreeNode) value).isSelected());
        label.setFont(tree.getFont());
        label.setText(stringValue);
        label.setSelected(selected);
        label.setFocus(hasFocus);

        if (leaf)
        {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        }
        else if (expanded)
        {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
        }
        else
        {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        return this;
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        return new Dimension(dCheck.width + dLabel.width,
                dCheck.height < dLabel.height ? dLabel.height : dCheck.height);
    }

    @Override
    public void doLayout()
    {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();

        int yCheck = 0;
        int yLabel = 0;

        if (dCheck.height < dLabel.height)
        {
            yCheck = (dLabel.height - dCheck.height) / 2;
        }
        else
        {
            yLabel = (dCheck.height - dLabel.height) / 2;
        }

        check.setLocation(0, yCheck);
        check.setBounds(0, yCheck, dCheck.width, dCheck.height);
        label.setLocation(dCheck.width, yLabel);
        label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
    }

    @Override
    public void setBackground(Color color)
    {
        if (color instanceof ColorUIResource)
        {
            color = null;
        }
        super.setBackground(color);
    }
}
