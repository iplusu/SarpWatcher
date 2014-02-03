/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
//import javax.xml.parsers.*;
import org.w3c.dom.*;
/**
 *
 * @author Administrator
 */
public class XmlParser {
    enum TYPE {NONE, THROUGHPUT, TRUST, PARENT};
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document document;
    private InputSource is;
    private float sent, received;

    public TYPE type;
    public int source, target, nodeId, parentSource, parentTarget;
    public long time;
    public int transaction; /* Should be changed to double type */
    public float throughput;
    public int nodeValue, isGood;
    public float trustFP, /*trustLP,*/ trustPR, overall;


    public XmlParser() {
        dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
            is = new InputSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        type = TYPE.NONE;
        time = 0;
        throughput = parentSource = parentTarget = source = target = nodeId = 0;
//        grb = brb = gfb = bfb = gab = bab = glb = blb = 0;
        nodeValue = -1;
        trustFP = /*trustLP = */trustPR = overall = 0;
    }

    public boolean parse(String xml) {
        try {
            is.setCharacterStream(new StringReader(xml.trim()));
            document = db.parse(is);
            return parseXml();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean parseXml() {
        try {
            if (document != null) {
                NodeList list = document.getElementsByTagName("throughput");
                if (list.getLength() != 0) {
                    type = TYPE.THROUGHPUT;
                } else {
                    list = document.getElementsByTagName("trust");
                    if (list.getLength() != 0) {
                        type = TYPE.TRUST;
                    } else {
                        list = document.getElementsByTagName("parent");
                        if (list.getLength() != 0) {
                            type = TYPE.PARENT;
                        } else {
                            type = TYPE.NONE;
                        }
                    }
                }

                Element mainElement = (Element)list.item(0);
                switch (type) {
                case TRUST:
                    transaction = Integer.parseInt(mainElement.getAttribute("transaction"));
                    time = Integer.parseInt(mainElement.getAttribute("time"));
                    source = Integer.parseInt(mainElement.getAttribute("source"));
                    target = Integer.parseInt(mainElement.getAttribute("target"));
                    isGood = Integer.parseInt(mainElement.getAttribute("isGood"));
                    String temp;
                    temp = mainElement.getAttribute("trustFP");
                    if (!temp.equals("nan"))
                        trustFP = Float.parseFloat(mainElement.getAttribute("trustFP"));
                    else
                        trustFP = 0.0F;
/*                    temp = mainElement.getAttribute("trustLP");
                    if (!temp.equals("nan"))
                        trustLP = Float.parseFloat(mainElement.getAttribute("trustLP"));
                    else
                        trustLP = 0.0F;
                    temp = mainElement.getAttribute("trustPR");
 */
                    if (!temp.equals("nan"))
                        trustPR = Float.parseFloat(mainElement.getAttribute("trustPR"));
                    else
                        trustPR = 0.0F;
                    temp = mainElement.getAttribute("overallTrust");
                    if (!temp.equals("nan"))
                        overall = Float.parseFloat(mainElement.getAttribute("overallTrust"));
                    else
                        overall = 0.0F;
                    break;
                case THROUGHPUT:
                    transaction = Integer.parseInt(mainElement.getAttribute("transaction"));
                    sent = Float.parseFloat(mainElement.getAttribute("sent"));
                    received = Float.parseFloat(mainElement.getAttribute("received"));
                    throughput = (received/sent) * 100;
                    break;
                case PARENT:
                    transaction = Integer.parseInt(mainElement.getAttribute("transaction"));
                    parentSource = Integer.parseInt(mainElement.getAttribute("source"));
                    parentTarget = Integer.parseInt(mainElement.getAttribute("target"));
                    break;
                case NONE:
                default:
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}