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

import javax.swing.tree.DefaultMutableTreeNode;

/** 
* @Class Name : CheckBoxTreeNode 
* @Description: Tree node for check box 
* @Author     : Dom Wang 
* @Email      : wisdomtool@outlook.com
* @Date       : Feb 26, 2018 3:53:19 PM 
* @Version    : Wisdom RESTClient V1.2 
*/
public class CheckBoxTreeNode extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 4611002678269395854L;

    protected boolean selected;

    /**
    * 
    * @Title      : CheckBoxTreeNode 
    * @Description: default constructor 
    * @Param      :
     */
    public CheckBoxTreeNode()
    {
        this(null);
    }

    /**
    * 
    * @Title      : CheckBoxTreeNode 
    * @Description: constructor 
    * @Param      : @param userObject
     */
    public CheckBoxTreeNode(Object userObject)
    {
        this(userObject, true, false);
    }

    /**
    * 
    * @Title      : CheckBoxTreeNode 
    * @Description: constructor 
    * @Param      : @param userObject
    * @Param      : @param allowsChildren
    * @Param      : @param selected
     */
    public CheckBoxTreeNode(Object userObject, boolean allowsChildren, boolean selected)
    {
        super(userObject, allowsChildren);
        this.selected = selected;
    }

    public boolean isSelected()
    {
        return selected;
    }

    /**
    * 
    * @Title      : setSelected 
    * @Description: Set node to be selected or unselected  
    * @Param      : @param selected 
    * @Return     : void
    * @Throws     :
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
        if (selected)
        {
            // If the node is selected, all of its child nodes are selected
            if (null != children)
            {
                for (Object child : children)
                {
                    CheckBoxTreeNode childNode = (CheckBoxTreeNode) child;
                    if (selected != childNode.isSelected())
                    {
                        childNode.setSelected(selected);
                    }
                }
            }

            /*
             * Check upwards, if all the child nodes of the parent node are
             * selected, then the parent node is also selected.
             */
            CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;

            /*
             * Begin to check whether all of the child nodes of the pNode are
             * selected
             */
            if (null != pNode)
            {
                int index = 0;
                for (; index < pNode.children.size(); ++index)
                {
                    CheckBoxTreeNode pChildNode = (CheckBoxTreeNode) pNode.children.get(index);
                    if (!pChildNode.isSelected())
                    {
                        break;
                    }
                }

                /*
                 * It shows that all the child nodes in pNode have been
                 * selected, then the parent node is selected. This method is a
                 * recursive method, so there is no need to iterate here,
                 * because the parent node itself will be checked upwards after
                 * the parent node is elected.
                 */
                if (index == pNode.children.size())
                {
                    if (pNode.isSelected() != selected)
                    {
                        pNode.setSelected(selected);
                    }
                }
            }
        }
        else
        {
            /*
             * If the child node is canceled when the parent node is cancelled,
             * then all the child nodes should be selected. Otherwise, the
             * cancellation of the parent node results in the cancellation of
             * the parent node. Then the cancellation of the parent node results
             * in the cancellation of the child node, but there is no need to
             * cancel the child node at this time.
             */
            if (null != children)
            {
                int index = 0;
                for (; index < children.size(); ++index)
                {
                    CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(index);
                    if (!childNode.isSelected())
                    {
                        break;
                    }
                }

                // Cancel from up to down
                if (index == children.size())
                {
                    for (int i = 0; i < children.size(); ++i)
                    {
                        CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(i);
                        if (childNode.isSelected() != selected)
                        {
                            childNode.setSelected(selected);
                        }
                    }
                }
            }

            /*
             * When check upwards, as long as there is a sub node that is not
             * selected, the parent should not be selected.
             */
            CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
            if (pNode != null && pNode.isSelected() != selected)
            {
                pNode.setSelected(selected);
            }
        }
    }
    
}
