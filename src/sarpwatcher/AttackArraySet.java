/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

/**
 *
 * @author Administrator
 */
public class AttackArraySet {
    enum TYPE {NONE, TRAFFICGENERATOR, SELECTIVEFORWARDER, LOOPCREATOR, NORESPONSE, ONOFFATTACKER};
    public TYPE type;
    public int target, victim, SF_numerator, SF_denominator, nGoodBehaviors, nBadBehaviors;

    public AttackArraySet() {
        type = TYPE.NONE;
        target = victim = SF_numerator = SF_denominator = nGoodBehaviors = nBadBehaviors = 0;
    }

    public void add(TYPE type, int src) {
//        if(type != TYPE.TRAFFICGENERATOR && type != TYPE.NORESPONSE){
//            new MessageBox("Warning","AttackArraySet : Wrong attack type");
//            return;
//        }
        this.type = type;
        this.target = src;
    }

    public void add(TYPE type, int src, int des) {
//        if(type != TYPE.LOOPCREATOR && type != TYPE.ONOFFATTACKER){
//            new MessageBox("Warning","AttackArraySet : Wrong attack type");
//            return;
//        }

        this.type = type;
        this.target = src;
        this.victim = des;
    }

    public void add(TYPE type, int src, int nGoodBehaviors, int nBadBehaviors){
        this.type = type;
        this.target = src;
        this.nGoodBehaviors = nGoodBehaviors;
        this.nBadBehaviors = nBadBehaviors;
    }

    public void add(TYPE type, int src, int des, int SF_numerator, int SF_denominator) {
//        if(type != TYPE.SELECTIVEFORWARDER){
//            new MessageBox("Warning","AttackArraySet : Wrong attack type");
//            return;
//        }

        this.type = type;
        this.target = src;
        this.victim = des;
        this.SF_numerator = SF_numerator;
        this.SF_denominator = SF_denominator;
    }
}
