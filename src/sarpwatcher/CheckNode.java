/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Administrator
 */
public class CheckNode extends DefaultMutableTreeNode {
    public final static int SINGLE_SELECTION = 0;
    public final static int DIG_IN_SELECTION = 4;
    protected int selectionMode;
    protected boolean isSelected;
    private String text;

    public CheckNode() {
        this(null);
    }

    public CheckNode(String userObject) {
        this(userObject, true, false);
        text = userObject;
    }

    public CheckNode(Object userObject, boolean allowsChildren , boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;
        setSelectionMode(DIG_IN_SELECTION);
    }


    public void setSelectionMode(int mode) {
        selectionMode = mode;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if ((selectionMode == DIG_IN_SELECTION) && (children != null) && !(text.equals("Nodes"))) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                CheckNode node = (CheckNode)e.nextElement();
                node.setSelected(isSelected);
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getText() {
        return text;
    }

    public CheckNode getChild(int node) {
        boolean isExist = false;
        CheckNode returnNode = null;
        for (int i = 0; i < getChildCount(); i++) {
            returnNode = (CheckNode)getChildAt(i);
            if (returnNode.getText().equals(String.valueOf(node))) {
                isExist = true;
                break;
            }
        }
        if (isExist) return returnNode;
        else        return null;
    }
}
