package cn;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class _2_Employees {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse("employees.xml");
        Element e = dom.getDocumentElement();
        NodeList ns = e.getElementsByTagName("Employee");
        System.out.println(ns.getLength());
        e = (Element) ns.item(2);
        ns = e.getElementsByTagName("Name");
        e = (Element) ns.item(0);
        System.out.println(e.getTextContent());
    }
}
