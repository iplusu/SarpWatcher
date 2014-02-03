/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.util.ArrayList;


/**
 *
 * @author Administrator
 */
public class Replayer {
    private String saveFile;
    private XmlReader xmlReader = null;
    private XmlWriter xmlWriter = null;

    public Replayer(String saveFile) {
        this.saveFile = saveFile;
    }

    public XmlReader readAll() {
        xmlReader = new XmlReader(saveFile);
        xmlReader.parse();
        return xmlReader;
    }

    public XmlReader readTopology() {
        xmlReader = new XmlReader(saveFile);
        xmlReader.parse();
        return xmlReader;
    }

    private void saveNssPath(String nssPath) {
        if (nssPath != null)
            xmlWriter.saveNssPath(nssPath);
    }

    private void saveThroughput(ArrayList<ThroughputArraySet> throughputSet) {
        if (!throughputSet.isEmpty())
            xmlWriter.saveThroughput(throughputSet);
    }

    private void saveTrust(ArrayList<TrustArraySet> trustSet) {
        if (!trustSet.isEmpty())
            xmlWriter.saveTrust(trustSet);
    }

    private void saveAttacker(ArrayList<AttackArraySet> attackSet) {
        if (!attackSet.isEmpty())
            xmlWriter.saveAttacks(attackSet);
    }

    public void saveAll(String nssPath, ArrayList<AttackArraySet> attackSet, ArrayList<ThroughputArraySet> throughputSet, ArrayList<TrustArraySet> trustSet, ArrayList<ParentArraySet> parentSet) {
        xmlWriter = new XmlWriter(saveFile);
        saveNssPath(nssPath);
        saveAttacker(attackSet);
        saveThroughput(throughputSet);
        saveTrust(trustSet);
        saveThroughputNotation(parentSet);
        xmlWriter.writeXml();
    }

    public void saveTopology(String nssPath, ArrayList<AttackArraySet> attackSet) {
        xmlWriter = new XmlWriter(saveFile);
        saveNssPath(nssPath);
        saveAttacker(attackSet);
        xmlWriter.writeXml();
    }

    private void saveThroughputNotation(ArrayList<ParentArraySet> parentSet) {
        if(!parentSet.isEmpty())
            xmlWriter.saveParent(parentSet);
    }
}
