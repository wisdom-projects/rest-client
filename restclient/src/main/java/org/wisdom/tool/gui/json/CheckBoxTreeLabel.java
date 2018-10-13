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
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/** 
* @Class Name : CheckBoxTreeLabel 
* @Description: Tree label for check box 
* @Author     : Dom Wang 
* @Email      : wisdomtool@qq.com
* @Date       : Feb 26, 2018 4:43:42 PM 
* @Version    : Wisdom RESTClient V1.2 
*/
public class CheckBoxTreeLabel extends JLabel
{
    private static final long serialVersionUID = -32204214662253992L;

    private boolean selected;

    private boolean hasFocus;

    public CheckBoxTreeLabel()
    {
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

    @Override
    public void paint(Graphics g)
    {
        String str = getText();
        if (null != str && !str.isEmpty())
        {
            if (selected)
            {
                g.setColor(UIManager.getColor("Tree.selectionBackground"));
            }
            else
            {
                g.setColor(UIManager.getColor("Tree.textBackground"));
            }

            Dimension d = getPreferredSize();
            int imageOffset = 0;
            Icon currentIcon = getIcon();
            if (null != currentIcon)
            {
                imageOffset = currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
            }

            g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
            if (hasFocus)
            {
                g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
            }
        }
        super.paint(g);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        if (null != d)
        {
            d = new Dimension(d.width + 3, d.height);
        }
        return d;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public void setFocus(boolean hasFocus)
    {
        this.hasFocus = hasFocus;
    }
}
