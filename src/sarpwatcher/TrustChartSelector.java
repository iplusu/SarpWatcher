/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Administrator
 */
public class TrustChartSelector extends Frame {
    private CheckNode rootNode = null;
    private JTree tree;
    private DefaultTreeModel tmModel;

    public TrustChartSelector(JFrame f) {
        //super(f, "Trust Chart Selector", false);
        super("Trust Chart Selector");

        rootNode = new CheckNode("Nodes");
        tree = new JTree(rootNode);
        tmModel = (DefaultTreeModel)tree.getModel();
        rootNode = (CheckNode)tmModel.getRoot();
        tree.setDoubleBuffered(true);
        tree.setCellRenderer(new CheckRenderer());
        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION
        );
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addMouseListener(new NodeSelectionListener(tree, f));
        JScrollPane sp = new JScrollPane(tree);
        sp.setSize(300, f.getHeight());

        this.setPreferredSize(new Dimension(300, f.getHeight()));
        this.setLocation(f.getX() + f.getHeight() + f.getWidth(), f.getY());
        this.add(sp, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    class NodeSelectionListener extends MouseAdapter {
        JTree tree;
        SarpWatcher sw;

        NodeSelectionListener(JTree tree, JFrame f) {
            this.tree = tree;
            this.sw = (SarpWatcher)f;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath  path = tree.getPathForRow(row);
            //TreePath  path = tree.getSelectionPath();
            if (path != null) {
                CheckNode node = (CheckNode)path.getLastPathComponent();
                boolean isSelected = ! (node.isSelected());
                node.setSelected(isSelected);
                if (node.getSelectionMode() == CheckNode.DIG_IN_SELECTION) {
                    CheckNode parent;
                    if ( isSelected ) {
                        tree.collapsePath(path);
                        tree.expandPath(path);
                        if (!node.getText().equals("Nodes")) {
                            if (node.getChildCount() == 0) {
                                parent = (CheckNode)node.getParent();
                                sw.displayTrustChart(Integer.parseInt(parent.getText()), Integer.parseInt(node.getText()));
                            }
                            else {
                                Enumeration en = node.children();
                                while (en.hasMoreElements()) {
                                    CheckNode tmpNode = (CheckNode)en.nextElement();
                                    parent = (CheckNode)tmpNode.getParent();
                                    sw.displayTrustChart(Integer.parseInt(parent.getText()), Integer.parseInt(tmpNode.getText()));
                                }
                            }
                        }
                    } else {
                        tree.collapsePath(path);
                        tree.expandPath(path);
                        if (!node.getText().equals("Nodes")) {
                            if (node.getChildCount() == 0) {
                                parent = (CheckNode)node.getParent();
                                sw.closeTrustChart(Integer.parseInt(parent.getText()), Integer.parseInt(node.getText()));
                            }
                            else {
                                Enumeration en = node.children();
                                while (en.hasMoreElements()) {
                                    CheckNode tmpNode = (CheckNode)en.nextElement();
                                    parent = (CheckNode)tmpNode.getParent();
                                    sw.closeTrustChart(Integer.parseInt(parent.getText()), Integer.parseInt(tmpNode.getText()));
                                }
                            }
                        }
                    }
                }
                ((DefaultTreeModel)tree.getModel()).nodeChanged(node);
                // I need revalidate if node is root.  but why?
                if (row == 0) {
                    tree.revalidate();
                    tree.repaint();
                }
            }
        }
    }

    public void addNode(int source, int target) {
        CheckNode sNode = null, tNode = null;
        int index = 0;
        sNode = rootNode.getChild(source);
        if (sNode == null) {
            sNode = new CheckNode(String.valueOf(source));
            if (rootNode.getChildCount() == 0) {
                index = 0;
            } else {
                Enumeration e = rootNode.children();
                while (e.hasMoreElements()) {
                    CheckNode tmpNode = (CheckNode)e.nextElement();
                    if (Integer.parseInt(tmpNode.getText()) < source) {
                        index++;
                    } else {
                        break;
                    }
                }
            }
//            if(index != 0)  index--;
//            rootNode.add(sNode);
            tmModel.insertNodeInto(sNode, rootNode, index);
//            for(int i = 0; i < rootNode.getChildCount(); i++)
//                tree.expandRow(i);
        }

        index = 0;
        tNode = sNode.getChild(target);
        if (tNode == null) {
            tNode = new CheckNode(String.valueOf(target));
//            sNode.add(tNode);
            if (sNode.getChildCount() == 0) {
                index = 0;
            } else {
                Enumeration e = sNode.children();
                while (e.hasMoreElements()) {
                    CheckNode tmpNode = (CheckNode)e.nextElement();
                    if (Integer.parseInt(tmpNode.getText()) < target) {
                        index++;
                    } else {
                        break;
                    }
                }
            }
//            if(index != 0)  index--;
            tmModel.insertNodeInto(tNode, sNode, index);
//            for(int i = 0; i < sNode.getChildCount(); i++)
//                tree.expandRow(i);
        }

//        for(int i = 0; i < rootNode.getChildCount(); i++)
//            tree.expandRow(i);
//
//        tmModel.nodeChanged(rootNode);
//        tree.revalidate();
//        tree.repaint();

    }
}