    public String getAllRecords(String attribute, String url) {
        String s = "";
        try {
            String str = "";
            String arr[] = new String[7];
            String[] subarr = new String[14];
            String[] subarr1 = new String[3];
            String mdPrefix = "", mdPrefixValue = "";
            String from = "", fromValue = "";
            String until = "", untilValue = "";
            String set = "", setValue = "";
            String retrieve = "", retrieveValue = "";
            String resToken = "", resTokenValue = "";
            java.util.Vector v = new java.util.Vector(1, 1);
            java.util.Vector v1 = new java.util.Vector(1, 1);
            java.util.Vector v3 = new java.util.Vector();
            Namespace markns = Namespace.getNamespace("http://www.loc.gov/MARC21/slim");
            String resValue = "0";
            String format = "";
            boolean illarg = false;
            boolean illMetaPref = false;
            boolean resTokenException = false;
            int count = 0;
            String illegalArg = "";
            Errors er = new Errors();
            String arguments = attribute.substring(attribute.indexOf("?") + 1);
            StringTokenizer st = new StringTokenizer(arguments, "&");
            int i = 0;
            int j = 0;
            int z = 0;
            String type = "";
            while (st.hasMoreTokens()) {
                arr[i] = st.nextToken();
                StringTokenizer subst = new StringTokenizer(arr[i], "=");
                while (subst.hasMoreTokens()) {
                    subarr[j] = subst.nextToken();
                    j++;
                }
                i++;
                count++;
            }
            int mdfCount = 0, fromCount = 0, untilCount = 0, setCount = 0, resTokCount = 0, retCount = 0;
            for (int k = 2; k < j; k += 2) {
                if (subarr[k].equals("metadataPrefix")) {
                    mdPrefix = "metadataPrefix";
                    mdPrefixValue = subarr[k + 1];
                    if (mdPrefixValue.equals("oai_dc") || mdPrefixValue.equals("marc21") || mdPrefixValue.equals("mods") || mdPrefixValue.equals("agris")) {
                        mdfCount++;
                    } else {
                        illMetaPref = true;
                    }
                } else if (subarr[k].equals("from")) {
                    from = "from";
                    fromCount++;
                    fromValue = subarr[k + 1];
                    try {
                        fromValue = validateDate(fromValue);
                    } catch (Exception e1) {
                        illarg = true;
                        illegalArg = illegalArg + "," + fromValue;
                    }
                } else if (subarr[k].equals("until")) {
                    until = "until";
                    untilCount++;
                    untilValue = subarr[k + 1];
                    try {
                        untilValue = validateDate(untilValue);
                    } catch (Exception e1) {
                        illarg = true;
                        illegalArg = illegalArg + "," + untilValue;
                    }
                } else if (subarr[k].equals("set")) {
                    set = "set";
                    setCount++;
                    setValue = subarr[k + 1];
                } else if (subarr[k].equals("retrieve")) {
                    retrieve = "set";
                    retCount++;
                    retrieveValue = subarr[k + 1];
                } else if (subarr[k].equals("resumptionToken")) {
                    int l = 0;
                    resToken = "resumptionToken";
                    resTokCount++;
                    resTokenValue = subarr[k + 1];
                    try {
                        StringTokenizer subst2 = new StringTokenizer(resTokenValue, "_");
                        while (subst2.hasMoreTokens()) {
                            subarr1[l] = subst2.nextToken();
                            l++;
                        }
                        resValue = subarr1[0];
                        int resValue1 = Integer.parseInt(resValue);
                        format = subarr1[1];
                    } catch (Exception e) {
                        illarg = true;
                        resTokenException = true;
                    }
                } else {
                    illarg = true;
                    illegalArg = illegalArg + "," + subarr[k];
                }
            }
            if (fromValue.equals("") && untilCount == 1) {
                fromValue = "0001-01-01";
            } else if (untilValue.equals("") && fromCount == 1) {
                String a = (new Resdate()).getDate();
                untilValue = a.substring(0, a.indexOf("T"));
            }
            if (resTokCount == 0) {
                resTokenValue = "0";
            }
            Namespace oains = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/");
            Element root = new Element("OAI-PMH", oains);
            Document doc = new Document(root);
            Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Attribute schemaLocation = new Attribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd", xsi);
            root.setAttribute(schemaLocation);
            root.addNamespaceDeclaration(xsi);
            Element responseDate = new Element("responseDate", oains);
            Resdate dt = new Resdate();
            responseDate.setText(dt.getDate());
            root.addContent(responseDate);
            Element request = new Element("request", oains);
            request.setAttribute("verb", "ListRecords");
            request.setText(url);
            if (!mdPrefix.equals("")) {
                request.setAttribute(mdPrefix, mdPrefixValue);
            }
            if (!from.equals("")) {
                request.setAttribute(from, fromValue);
            }
            if (!until.equals("")) {
                request.setAttribute(until, untilValue);
            }
            if (!set.equals("")) {
                request.setAttribute(set, setValue);
            }
            if (!resToken.equals("")) {
                request.setAttribute(resToken, resTokenValue);
            }
            root.addContent(request);
            Element listRec = new Element("ListRecords", oains);
            if (count > 7 || count < 1 || (mdfCount < 1 && resTokenValue.equals("0")) || mdfCount > 1 || fromCount > 1 || untilCount > 1 || setCount > 1 || resTokCount > 1 || illarg == true || (!resTokenValue.equals("0") && (mdfCount == 1 || fromCount == 1 || untilCount == 1 || setCount == 1))) {
                if (mdfCount < 1 && !illMetaPref) {
                    Element errorXML = er.describeError(2, "missing argument:metadataPrefix ", url, "ListIdentifiers");
                    root.addContent(errorXML);
                }
                if (illMetaPref) {
                    Element errorXML = er.describeError(6, "& for " + mdPrefixValue, url, "ListRecords");
                    root.addContent(errorXML);
                }
                if (resTokenException) {
                    Element errorXML = er.describeError(4, "  badresumption token" + resTokenValue, url, "ListRecords");
                    root.addContent(errorXML);
                }
                if (illarg || (!resTokenValue.equals("0") && (mdfCount == 1 || fromCount == 1 || untilCount == 1 || setCount == 1)) || mdfCount > 1 || fromCount > 1 || untilCount > 1 || setCount > 1 || resTokCount > 1) {
                    Element errorXML = er.describeError(2, illegalArg.substring(illegalArg.indexOf(",") + 1), url, "ListRecords");
                    root.addContent(errorXML);
                }
            } else if ((mdfCount == 1 && resValue.equals("0")) || (resValue.equals("0") == false)) {
                try {
                    v = ((ejb.bprocess.OAIPMH.ListGetRecordsHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("ListGetRecords")).create().listRecords(mdPrefixValue, fromValue, untilValue, setValue, resValue, retrieveValue);
                    v1 = ((ejb.bprocess.OAIPMH.ListGetRecordsHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("ListGetRecords")).create().getIdentifiers(mdPrefixValue, fromValue, untilValue, setValue, resValue, retrieveValue);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (v.size() == 1 || (format.equals("") == true && resValue.equals("0") == false)) {
                    Element errorXML = er.describeError(4, "  badresumption token" + resTokenValue, url, "ListRecords");
                    root.addContent(errorXML);
                } else if (v.size() == 2) {
                    Element errorXML = er.describeError(5, "No records ", url, "ListRecords");
                    root.addContent(errorXML);
                } else {
                    int noOfRec = 0;
                    for (int p = 2, k = 2; p < v1.size(); k = k + 2, p = p + 3) {
                        Element record = new Element("record", oains);
                        Element head = new Element("header", oains);
                        Element id = new Element("identifier", oains);
                        Element date = new Element("datestamp", oains);
                        id.setText(v1.elementAt(p).toString());
                        head.addContent(id);
                        java.util.Vector vSet = new java.util.Vector(1, 1);
                        date.addContent(v1.elementAt(p + 2).toString().substring(0, 10));
                        head.addContent(date);
                        vSet = (java.util.Vector) v1.elementAt(p + 1);
                        for (int y = 0; y < vSet.size(); y++) {
                            Element set1 = new Element("setSpec", oains);
                            set1.setText(vSet.elementAt(y).toString());
                            head.addContent(set1);
                        }
                        record.addContent(head);
                        Element metadata = new Element("metadata", oains);
                        Element marcroot = new Element("record", "marc", "http://www.loc.gov/MARC21/slim");
                        marcroot.addNamespaceDeclaration(xsi);
                        Attribute schemaLocationmarc = new Attribute("schemaLocation", "http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd", xsi);
                        marcroot.setAttribute(schemaLocationmarc);
                        marcroot.setAttribute("type", "Bibliographic");
                        v3 = (java.util.Vector) v.elementAt(k);
                        java.util.Vector vL = (java.util.Vector) v3.elementAt(z);
                        org.jdom.Element lead = new org.jdom.Element("leader", "marc", "http://www.loc.gov/MARC21/slim");
                        lead.setText(vL.elementAt(0).toString());
                        marcroot.addContent(lead);
                        java.util.Vector vC = (java.util.Vector) v3.elementAt(z + 1);
                        for (int u = 0; u < vC.size(); u = u + 2) {
                            org.jdom.Element ct = new org.jdom.Element("controlfield", "marc", "http://www.loc.gov/MARC21/slim");
                            ct.setAttribute("tag", vC.elementAt(u).toString());
                            ct.setText(vC.elementAt(u + 1).toString());
                            marcroot.addContent(ct);
                        }
                        z = z + 2;
                        String dataFieldRec = v.elementAt(k + 1).toString();
                        try {
                            org.jdom.input.SAXBuilder sab1 = new org.jdom.input.SAXBuilder();
                            org.jdom.Document doc1 = sab1.build(new java.io.StringReader(dataFieldRec));
                            org.jdom.Namespace mns = org.jdom.Namespace.getNamespace("marc");
                            java.util.List dfList = doc1.getRootElement().getChildren();
                            for (int a = 0; a < dfList.size(); a++) {
                                org.jdom.Element datafield = new org.jdom.Element("datafield", "marc", "http://www.loc.gov/MARC21/slim");
                                org.jdom.Element datafield1 = (org.jdom.Element) dfList.get(a);
                                java.util.List lsSub = datafield1.getChildren();
                                String tag = datafield1.getAttributeValue("tag").toString();
                                datafield.setAttribute("tag", tag);
                                datafield.setAttribute("ind1", datafield1.getAttributeValue("ind1").toString());
                                datafield.setAttribute("ind2", datafield1.getAttributeValue("ind2").toString());
                                for (int x = lsSub.size() - 1; x >= 0; x--) {
                                    org.jdom.Element sub = new org.jdom.Element("subfield", "marc", "http://www.loc.gov/MARC21/slim");
                                    org.jdom.Element subf = (org.jdom.Element) lsSub.get(x);
                                    sub.setAttribute("code", subf.getAttributeValue("code"));
                                    sub.setText(subf.getText());
                                    datafield.addContent(sub);
                                }
                                marcroot.addContent(datafield);
                            }
                            if (mdPrefixValue.equals("oai_dc") || format.equals("dc")) {
                                try {
                                    java.util.Properties systemSettings = System.getProperties();
                                    java.util.prefs.Preferences prefs = java.util.prefs.Preferences.systemRoot();
                                    if (prefs.getBoolean("useproxy", false)) {
                                        systemSettings.put("proxySet", "true");
                                        systemSettings.put("proxyHost", prefs.get("proxyservername", ""));
                                        systemSettings.put("proxyPort", prefs.get("proxyport", ""));
                                        systemSettings.put("http.proxyHost", prefs.get("proxyservername", ""));
                                        systemSettings.put("http.proxyPort", prefs.get("proxyport", ""));
                                    }
                                    String urltext = "";
                                    Transformer transformer = null;
                                    urltext = "http://www.loc.gov/standards/marcxml/xslt/MARC21slim2OAIDC.xsl";
                                    transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.io.File.separator + "StyleSheets" + java.io.File.separator + "MARC21slim2OAIDC.xsl"));
                                    Document docmarc = new Document(marcroot);
                                    JDOMSource in = new JDOMSource(docmarc);
                                    JDOMResult out = new JDOMResult();
                                    transformer.transform(in, out);
                                    Document doc2 = out.getDocument();
                                    org.jdom.output.XMLOutputter out1 = new org.jdom.output.XMLOutputter();
                                    out1.setTextTrim(true);
                                    out1.setIndent("  ");
                                    out1.setNewlines(true);
                                    Element dcroot1 = doc2.getRootElement();
                                    Namespace xsi1 = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                                    Namespace oainsdc = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
                                    Element dcroot = new Element("dc", "oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
                                    Namespace dcns = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
                                    dcroot.addNamespaceDeclaration(dcns);
                                    dcroot.addNamespaceDeclaration(xsi1);
                                    Attribute schemaLocationdc = new Attribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd", xsi1);
                                    dcroot.setAttribute(schemaLocationdc);
                                    java.util.List dcList = doc2.getRootElement().getChildren();
                                    for (int g = 0; g < dcList.size(); g++) {
                                        Element dcElem1 = (org.jdom.Element) dcList.get(g);
                                        Element dcElem = new Element(dcElem1.getName(), "dc", "http://purl.org/dc/elements/1.1/");
                                        dcElem.setText(dcElem1.getText());
                                        dcroot.addContent(dcElem);
                                    }
                                    metadata.addContent(dcroot);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (mdPrefixValue.equals("mods") || format.equals("mods")) {
                                try {
                                    java.util.Properties systemSettings = System.getProperties();
                                    java.util.prefs.Preferences prefs = java.util.prefs.Preferences.systemRoot();
                                    if (prefs.getBoolean("useproxy", false)) {
                                        systemSettings.put("proxySet", "true");
                                        systemSettings.put("proxyHost", prefs.get("proxyservername", ""));
                                        systemSettings.put("proxyPort", prefs.get("proxyport", ""));
                                        systemSettings.put("http.proxyHost", prefs.get("proxyservername", ""));
                                        systemSettings.put("http.proxyPort", prefs.get("proxyport", ""));
                                    }
                                    String urltext = "";
                                    Transformer transformer = null;
                                    urltext = "http://www.loc.gov/standards/mods/v3/MARC21slim2MODS3.xsl";
                                    java.net.URL url1 = new java.net.URL(urltext);
                                    java.net.URLConnection urlconn = url1.openConnection();
                                    urlconn.setDoInput(true);
                                    transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.io.File.separator + "StyleSheets" + java.io.File.separator + "MARC21slim2MODS3.xsl"));
                                    Document docmarc = new Document(marcroot);
                                    JDOMSource in = new JDOMSource(docmarc);
                                    JDOMResult out = new JDOMResult();
                                    transformer.transform(in, out);
                                    Document doc2 = out.getDocument();
                                    Namespace xsi1 = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");
                                    Namespace oainsdc = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
                                    Element mroot = new Element("mods", "http://www.loc.gov/mods/v3");
                                    Namespace dcns = Namespace.getNamespace("http://www.loc.gov/mods/v3");
                                    mroot.addNamespaceDeclaration(xsi1);
                                    Attribute schemaLocationdc = new Attribute("schemaLocation", "http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-0.xsd", xsi1);
                                    mroot.setAttribute(schemaLocationdc);
                                    java.util.List dcList = doc2.getRootElement().getChildren();
                                    for (int g = 0; g < dcList.size(); g++) {
                                        Element mElem1 = (org.jdom.Element) dcList.get(g);
                                        Element mElem = new Element(mElem1.getName(), "http://www.loc.gov/mods/v3");
                                        if (mElem1.hasChildren()) {
                                            java.util.List mList1 = mElem1.getChildren();
                                            for (int f = 0; f < mList1.size(); f++) {
                                                Element mElem2 = (org.jdom.Element) mList1.get(f);
                                                Element mElem3 = new Element(mElem2.getName(), "http://www.loc.gov/mods/v3");
                                                if (mElem2.hasChildren()) {
                                                    java.util.List mList2 = mElem2.getChildren();
                                                    for (int h = 0; h < mList2.size(); h++) {
                                                        Element mElem4 = (org.jdom.Element) mList1.get(h);
                                                        Element mElem5 = new Element(mElem4.getName(), "http://www.loc.gov/mods/v3");
                                                        mElem5.setText(mElem4.getText());
                                                        mElem3.addContent(mElem5);
                                                    }
                                                }
                                                if (mElem2.hasChildren() == false) {
                                                    mElem3.setText(mElem2.getText());
                                                }
                                                mElem.addContent(mElem3);
                                            }
                                        }
                                        if (mElem1.hasChildren() == false) {
                                            mElem.setText(mElem1.getText());
                                        }
                                        mroot.addContent(mElem);
                                    }
                                    metadata.addContent(mroot);
                                    type = "_mods";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (mdPrefixValue.equals("agris") || format.equals("agris")) {
                                org.jdom.Element agsRoot = (new helper.oaipmhreq.MARC21ToArgrisConverter()).convert(marcroot, v1.elementAt(p).toString());
                                metadata.addContent(agsRoot);
                            }
                        } catch (org.jdom.JDOMException exc) {
                        }
                        if (mdPrefixValue.equals("marc21") || format.equals("marc")) {
                            metadata.addContent(marcroot);
                            type = "_marc";
                        } else if (mdPrefixValue.equals("oai_dc") || format.equals("dc")) {
                            type = "_dc";
                        }
                        record.addContent(metadata);
                        listRec.addContent(record);
                        noOfRec++;
                    }
                    Integer nextRes = new Integer(resValue);
                    if (noOfRec == 100) {
                        noOfRec = nextRes.intValue() + 100;
                        Element resumptionToken = new Element("resumptionToken", oains);
                        String res = new Integer(noOfRec).toString();
                        resumptionToken.setText(res + type);
                        listRec.addContent(resumptionToken);
                    }
                    root.addContent(listRec);
                }
            }
            XMLOutputter out = new XMLOutputter();
            out.setIndent(" ");
            out.setNewlines(true);
            s = out.outputString(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
