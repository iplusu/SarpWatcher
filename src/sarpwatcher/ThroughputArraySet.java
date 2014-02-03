/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

/**
 *
 * @author Administrator
 */
public class ThroughputArraySet {
    public int transaction;
    public float throughput;
    //public int source, target;

    public ThroughputArraySet() {
        this.throughput = 0;
        this.transaction = 0;
        //this.source = source;
        //this.target = target;

        //transaction = new ArrayList();
        //throughput = new ArrayList();
    }

    public void add(int transaction, float throughput) {
        this.transaction = transaction;
        this.throughput = throughput;
    }
}
