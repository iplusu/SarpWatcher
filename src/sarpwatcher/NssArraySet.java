/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;
import java.util.LinkedList;
/**
 *
 * @author Administrator
 */
public class NssArraySet {
    private LinkedList list;
    private int sourceNode;

    public NssArraySet(int sourceNode) {
        this.sourceNode = sourceNode;
        list = new LinkedList();
    }

    public void add(int tNode) {
        list.add(tNode);
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public int[] getTargetNodes() {
        int size = list.size();
        int[] tNodes = new int[size];
        for (int i = 0; i < size; i++) {
            tNodes[i] = (Integer)list.get(i);
        }
        return tNodes;
    }

    public int getLength() {
        return list.size();
    }
}
