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
public class ParentArraySet {
    private int transaction;
    private LinkedList<ParentNodeList> aParent;
    private boolean isMarked = false;

    public ParentArraySet(int transaction){
        this.transaction = transaction;
        aParent = new LinkedList<ParentNodeList>();
    }

    public void setMark(boolean b){
        this.isMarked = b;
    }

    public boolean getMark(){
        return isMarked;
    }

    public void add(int source, int target){
        ParentNodeList pnl = new ParentNodeList();
        pnl.source = source;
        pnl.target = target;
        aParent.add(pnl);
    }

    public String NodeMapString(int index){
        return String.format("%dto%d", aParent.get(index).source, aParent.get(index).target);
    }

    public int size(){
        return aParent.size();
    }

    public int getTransaction(){
        return this.transaction;
    }
    
    public boolean isSameTransaction(int transaction){
        if(this.transaction == transaction){
            return true;
        }
        else return false;
    }

    public boolean isNewTransaction(int transaction){
        if(this.transaction < transaction){
            return true;
        }
        return false;
    }

    public boolean isRouteExist(int source, int target){
        int size = aParent.size();
        for(int i = 0; i < size; i++){
            ParentNodeList pnl = aParent.get(i);
            if(pnl.source == source && pnl.target == target)
                return true;
        }
        return false;
    }

    public boolean isReachedBS(){
        if(aParent.get(aParent.size() - 1).target == 0)
            return true;
        else
            return false;
    }

    public int getSource(int index){
        return aParent.get(index).source;
    }

    public int getTarget(int index){
        return aParent.get(index).target;
    }
}

class ParentNodeList{
    public int source;
    public int target;
}