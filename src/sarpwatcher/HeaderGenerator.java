/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
/**
 *
 * @author Administrator
 */
public class HeaderGenerator {
    public enum TYPE {TRAFFIC_GENERATOR, SELECTIVE_FORWARD_SRC, SELECTIVE_FORWARD_DES,
                      SELECTIVE_FORWARD_NUMERATOR, SELECTIVE_FORWARD_DENOMINATOR,
                      LOOP_CREATOR_SRC, LOOP_CREATOR_DES, ON_OFF, NO_RESPONSE, NUM_THIRTY
                     };
    private String trafficFile, attackFile;
    private ArrayList<Integer> trafficGenerator, SF_src, SF_des, SF_num, SF_den, LC_src, LC_des, onOff, noRes, nGoodBehaviors, nBadBehaviors;
    private ArrayList<AttackArraySet> aas;

    public HeaderGenerator(File f, ArrayList<AttackArraySet> aas) {
        this.trafficFile = f.getAbsoluteFile() + "/TrafficGeneratorH.h";
        this.attackFile = f.getAbsoluteFile() + "/AttackH.h";
        this.aas = aas;
    }

    private void initArrays() {
        trafficGenerator = new ArrayList<Integer>();
        SF_src = new ArrayList<Integer>();
        SF_des = new ArrayList<Integer>();
        SF_num = new ArrayList<Integer>();
        SF_den = new ArrayList<Integer>();
        LC_src = new ArrayList<Integer>();
        LC_des = new ArrayList<Integer>();
        onOff = new ArrayList<Integer>();
        noRes = new ArrayList<Integer>();
        nGoodBehaviors = new ArrayList<Integer>();
        nBadBehaviors = new ArrayList<Integer>();
    }

    private void splitArray() {
        initArrays();
        for (int i = 0; i < aas.size(); i++) {
            AttackArraySet tmpAas = aas.get(i);
            if (tmpAas.type == AttackArraySet.TYPE.TRAFFICGENERATOR) {
                trafficGenerator.add(tmpAas.target);
            } else if (tmpAas.type == AttackArraySet.TYPE.SELECTIVEFORWARDER) {
                SF_src.add(tmpAas.target);
                SF_des.add(tmpAas.victim);
                SF_num.add(tmpAas.SF_numerator);
                SF_den.add(tmpAas.SF_denominator);
            } else if (tmpAas.type == AttackArraySet.TYPE.LOOPCREATOR) {
                LC_src.add(tmpAas.target);
                LC_des.add(tmpAas.victim);
            } else if (tmpAas.type == AttackArraySet.TYPE.NORESPONSE) {
                noRes.add(tmpAas.target);
            } else if (tmpAas.type == AttackArraySet.TYPE.ONOFFATTACKER) {
                onOff.add(tmpAas.target);
                nGoodBehaviors.add(tmpAas.nGoodBehaviors);
                nBadBehaviors.add(tmpAas.nBadBehaviors);
            }
        }
    }

    public void writeHeader() {
        try {
            splitArray();
            writeTraffic();
            writeAttack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeAttack() {
        FileWriter fwAttack;
        BufferedWriter bwAttack;
        int count;
        StringBuilder content = new StringBuilder();

        try {
            fwAttack = new FileWriter(attackFile, false);
            bwAttack = new BufferedWriter(fwAttack, 1024);

            content.append("/*\n");
            content.append(" *  File:\tTrafficGeneratorH.h\n");
            content.append(" *  Author:\tKenneth Rahn Jr\n");
            content.append(" *  Created:\tMay 20, 2009\n");
            content.append(" */\n\n");
            content.append("#ifndef ATTACK_H\n");
            content.append("#define ATTACK_H\n\n");
            content.append("/* BEGIN SELECTIVE FORWARDING PARAMETERS */\n\n");
            content.append("/* Example SF setup\n");
            content.append(" *\n");
            content.append(" * Let's say we want node 22 to selectively forward 50% of node 23's packets and\n");
            content.append(" * node 17 to selectively forward 60% of node 22's packets.\n *\n");
            content.append(" * uint16_t selectiveForwardeAttackers[]   = { 22, 17 };\n");
            content.append(" * uint16_t selectiveForwardVictims[]      = { 23, 22 };\n");
            content.append(" * uint16_t selectiveforwardNumerator[]    = {  1,  3 };\n");
            content.append(" * uint16_t selectiveForwardDenomenators[] = {  2,  5 };\n");
            content.append(" *\n */\n\n");
            content.append("uint16_t aCounter;\n\n");

            content.append("uint16_t selectiveForwardAttackers[]    = ");
            count = SF_src.size();
            content.append(list2Array(SF_src));

            content.append("uint16_t selectiveForwardVictims[]      = ");
            count = SF_des.size();
            content.append(list2Array(SF_des));

            content.append("uint16_t selectiveForwardNumerators[]   = ");
            count = SF_num.size();
            content.append(list2Array(SF_num));

            content.append("uint16_t selectiveForwardDenomenators[] = ");
            count = SF_den.size();
            content.append(list2Array(SF_den));
            content.append("\n");

            content.append(String.format("uint16_t numSFAttackers                 = %d;\n\n", count));
            content.append("/* We dont' use this yet, but we should. */\n");
            content.append("typedef struct selectiveForwarder {\n");
            content.append("  uint16_t attacker_id;\n");
            content.append("  uint16_t victim_id;\n");
            content.append("  uint16_t numerator;\n");
            content.append("  uint16_t demonenator;\n");
            content.append("} selectiveForwarder;\n");

            content.append("/* END SELECTIVE FORWARDING PARAMETERS */\n\n\n");
            content.append("/* BEGIN LOOP CREATION PARAMETERS */\n");

            content.append("uint16_t loopCreationAttackers[]            = ");
            count = LC_src.size();
            content.append(list2Array(LC_src));

            content.append("uint16_t loopCreationAttackersParents[]     = ");
            count = LC_des.size();
            content.append(list2Array(LC_des));

            content.append("//uint16_t loopCreationAttackers[]            = {  8,  7, 12 }\n");
            content.append("//uint16_t loopCreationAttackersParents[]     = {  7, 12, 13 }\n\n");

            content.append(String.format("uint16_t numLoopCreationAttackers           = %d;\n", count));
            content.append("/* END LOOP CREATION PARAMETERS */\n\n\n");

            content.append("/* BEGIN NO RESPONSE PARAMETERS */\n");
            content.append("uint16_t noResponseAttackers[]            = ");
            count = noRes.size();
            content.append(list2Array(noRes));

            content.append(String.format("uint16_t numNoResponseAttackers           = %d;\n", count));
            content.append("/* END NO RESPONSE PARAMETERS */\n\n\n");

            content.append("/* BEGIN ON / OFF PARAMETERS */\n");
            content.append("uint16_t onOffAttackers[]                 = ");
            count = onOff.size();
            content.append(list2Array(onOff));

            content.append("uint16_t nGoodBehaviors[]                 = ");
            count = nGoodBehaviors.size();
            content.append(list2Array(nGoodBehaviors));

            content.append("uint16_t nBadBehaviors[]                  = ");
            count = nBadBehaviors.size();
            content.append(list2Array(nBadBehaviors));

            content.append(String.format("uint16_t numOnOffAttackers                = %d;\n", count));
            content.append("/* END ON / OFF PARAMETERS */\n\n");

            content.append("#endif");

            bwAttack.write(content.toString());
            bwAttack.newLine();
            bwAttack.flush();
            bwAttack.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeTraffic() {
        FileWriter fwTraffic;
        BufferedWriter bwTraffic;
        int count;
        StringBuilder content = new StringBuilder();

        try {
            fwTraffic = new FileWriter(trafficFile, false);
            bwTraffic = new BufferedWriter(fwTraffic, 1024);

            content.append("/*\n");
            content.append(" *  File:\tTrafficGeneratorH.h\n");
            content.append(" *  Author:\tKenneth Rahn Jr\n");
            content.append(" *  Created:\tMay 22, 2009\n");
            content.append(" */\n\n");
            content.append("#ifndef TRAFFIC_GENERATOR_H\n");
            content.append("#define TRAFFIC_GENERATOR_H\n\n");
            content.append("uint16_t tfLoopCounter;\n");

            content.append("uint16_t trafficGenerators[] = ");
            count = trafficGenerator.size();
            content.append(list2Array(trafficGenerator));
            content.append(String.format("uint16_t numTrafficGenerators = %d;", count));

            content.append("\n\n#endif");

            bwTraffic.write(content.toString());
            bwTraffic.newLine();
            bwTraffic.flush();
            bwTraffic.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private String list2Array(ArrayList<Integer> array) {
        int size = array.size();
        StringBuilder content = new StringBuilder();
        content.append("{ ");
        for (int i = 0; i < size; i++) {
            if (i == size - 1)
                content.append(array.get(i));
            else
                content.append(array.get(i) + ", ");
        }
        content.append(" };\n");
        return content.toString();
    }
}
