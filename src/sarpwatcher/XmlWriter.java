/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.OutputKeys;
import java.io.*;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class XmlWriter {
    private String saveFile;
    private Document doc;
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder builder;
    private Element element;
    private Writer writer;
    private StreamResult result;

    public XmlWriter(String saveFile) {
        this.saveFile = saveFile;
        try {
            writer = new StringWriter();
            result = new StreamResult(writer);
            this.docFactory = DocumentBuilderFactory.newInstance();
            this.builder = this.docFactory.newDocumentBuilder();
            this.doc = this.builder.newDocument();
            this.element = this.doc.createElement("sarp");
            this.doc.appendChild(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeXml() {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer trans = tFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.METHOD, "xml");
            trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
//            trans.transform(new DOMSource(doc), new StreamResult(System.out)); trans.transform(new DOMSource(doc), result);
            trans.transform(new DOMSource(doc), new StreamResult(new FileWriter(this.saveFile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNssPath(String nssPath) {
        Element tmp = this.doc.createElement("nss");
        tmp.setAttribute("path", nssPath);
        this.element.appendChild(tmp);
    }

    public void saveThroughput(ArrayList<ThroughputArraySet> throughput) {
        for (int i = 0; i < throughput.size(); i++) {
            Element tmp = this.doc.createElement("throughput");
            ThroughputArraySet tps = (ThroughputArraySet)throughput.get(i);
            tmp.setAttribute("transaction", String.valueOf(tps.transaction));
            tmp.setAttribute("value", String.valueOf(tps.throughput));
            this.element.appendChild(tmp);
        }
    }

    public void saveParent(ArrayList<ParentArraySet> parentSet){
        for(int i = 0; i < parentSet.size(); i++){
            ParentArraySet pas = (ParentArraySet)parentSet.get(i);
            for(int j = 0; j < pas.size(); j++){
                Element tmp = this.doc.createElement("parent");
                tmp.setAttribute("transaction", String.valueOf(pas.getTransaction()));
                tmp.setAttribute("source", String.valueOf(pas.getSource(j)));
                tmp.setAttribute("target", String.valueOf(pas.getTarget(j)));
                this.element.appendChild(tmp);
            }
        }
    }

    public void saveTrust(ArrayList<TrustArraySet> trust) {
        for (int i = 0; i < trust.size(); i++) {
            Element tmp = this.doc.createElement("trust");
            TrustArraySet tas = (TrustArraySet)trust.get(i);
            tmp.setAttribute("transaction", String.valueOf(tas.transaction));
            tmp.setAttribute("time", String.valueOf(tas.time));
            tmp.setAttribute("source", String.valueOf(tas.source));
            tmp.setAttribute("target", String.valueOf(tas.target));
            tmp.setAttribute("trustFP", String.valueOf(tas.trustFP));
//            tmp.setAttribute("trustLP", String.valueOf(tas.trustLP));
            tmp.setAttribute("trustPR", String.valueOf(tas.trustPR));
            tmp.setAttribute("overall", String.valueOf(tas.overall));
            this.element.appendChild(tmp);
        }
    }

    public void saveAttacks(ArrayList<AttackArraySet> aas) {
        for (int i = 0; i < aas.size(); i++) {
            Element tmp = null;
            AttackArraySet tmpAas = aas.get(i);
            if (tmpAas.type == AttackArraySet.TYPE.TRAFFICGENERATOR) {
                tmp = this.doc.createElement("tg");
                tmp.setAttribute("target", String.valueOf(tmpAas.target));
            } else if (tmpAas.type == AttackArraySet.TYPE.SELECTIVEFORWARDER) {
                tmp = this.doc.createElement("sf");
                tmp.setAttribute("target", String.valueOf(tmpAas.target));
                tmp.setAttribute("victim", String.valueOf(tmpAas.victim));
                tmp.setAttribute("num", String.valueOf(tmpAas.SF_numerator));
                tmp.setAttribute("den", String.valueOf(tmpAas.SF_denominator));
            } else if (tmpAas.type == AttackArraySet.TYPE.LOOPCREATOR) {
                tmp = this.doc.createElement("lc");
                tmp.setAttribute("target", String.valueOf(tmpAas.target));
                tmp.setAttribute("victim", String.valueOf(tmpAas.victim));
            } else if (tmpAas.type == AttackArraySet.TYPE.NORESPONSE) {
                tmp = this.doc.createElement("nr");
                tmp.setAttribute("target", String.valueOf(tmpAas.target));
            } else if (tmpAas.type == AttackArraySet.TYPE.ONOFFATTACKER) {
                tmp = this.doc.createElement("of");
                tmp.setAttribute("target", String.valueOf(tmpAas.target));
                tmp.setAttribute("nGoodBehaviors", String.valueOf(tmpAas.nGoodBehaviors));
                tmp.setAttribute("nBadBehaviors", String.valueOf(tmpAas.nBadBehaviors));
            }
            if (tmp != null)
                this.element.appendChild(tmp);
        }
    }
}