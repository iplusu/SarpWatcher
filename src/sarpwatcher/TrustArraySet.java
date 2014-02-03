/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

/**
 *
 * @author Administrator
 */
public class TrustArraySet {
    public long time;
    public int transaction;/*, gfb, bfb, grb, brb, gab, bab, glb, blb;*/
    public float trustFP, /*trustLP,*/ trustPR, overall;
    public int source, target;

    public TrustArraySet(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public void addTrustValues(int transaction, long time, float trustFP, /* float trustLP,*/ float trustPR, float overall) {
        this.transaction = transaction;
        this.time = time;
        this.trustFP = trustFP;
//        this.trustLP = trustLP;
        this.trustPR = trustPR;
        this.overall = overall;
    }
}
