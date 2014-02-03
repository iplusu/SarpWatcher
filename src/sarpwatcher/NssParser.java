/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.LinkedList;
/**
 *
 * @author Administrator
 */
public class NssParser {
    private String nssFile;
    private File file;
    private NodeMap nm;
    private LinkedList nssList;

    public NssParser() {
        file = null;
        nm = null;
        nssList = null;
    }

    public NssParser(String nssFile, NodeMap n) {
        this.nssFile = nssFile;
        this.file = new File(nssFile);
        this.nm = n;
        this.nssList = new LinkedList();
    }

    public String getFilePath() {
        if (file == null)    return null;
        else                return file.getAbsolutePath();
    }

    public void start() {
        this.parse();
        this.display();
        nm.finalizeGraph();
    }

    private void display() {
        int i = 0;
        for (i = 0; i < nssList.size(); i++) {
            NssArraySet nssSet = (NssArraySet)nssList.get(i);
            nm.addNode(nssSet.getSourceNode());
        }
        for (i = 0; i < nssList.size(); i++) {
            NssArraySet nssSet = (NssArraySet)nssList.get(i);
            int[] tNodes = nssSet.getTargetNodes();
            for (int j = 0; j < nssSet.getLength(); j++) {
                nm.addEdge(nssSet.getSourceNode(), tNodes[j]);
            }
        }
    }

    private void parse() {
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr, 1024);
            String buffer = br.readLine();
            while (buffer != null) {
                buffer.trim();
                StringTokenizer nodeToken = new StringTokenizer(buffer, ":");
                int sNode = Integer.parseInt(nodeToken.nextToken());
                String tmp1 = nodeToken.nextToken();
                int tNode = 0;
                Float lNode = 0F;
                if (nodeToken.countTokens() == 1) {
                    tNode = Integer.parseInt(tmp1);
                    lNode = Float.parseFloat(nodeToken.nextToken());
                } else {
                    StringTokenizer linkToken = new StringTokenizer(tmp1, " ");
                    tNode = Integer.parseInt(linkToken.nextToken());
                    lNode = Float.parseFloat(linkToken.nextToken());
                }
                NssArraySet nssSet;

                if (lNode != 1) {
                    nssSet = findList(sNode);
                    if (nssSet == null) {
                        nssSet = new NssArraySet(sNode);
                        nssSet.add(tNode);
                        nssList.add(nssSet);
                    } else {
                        nssSet.add(tNode);
                    }
                }

                buffer = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NssArraySet findList(int sNode) {
        NssArraySet nss = null;
        for (int i = 0; i < nssList.size(); i++) {
            nss = (NssArraySet)nssList.get(i);
            if (nss.getSourceNode() == sNode) {
                break;
            } else {
                nss = null;
            }
        }
        return nss;
    }
}
