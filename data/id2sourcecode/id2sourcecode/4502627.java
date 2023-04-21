    public void getDetails() {
        String fr = "";
        try {
            boolean resExists = true;
            String resT = null;
            String folPath = "";
            int o = 0;
            org.jdom.Element op = new org.jdom.Element("OperationId");
            op.setAttribute("no", "34");
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
            String xml34 = out.outputString(op);
            System.out.println("xml is" + xml34);
            String samp = newgen.presentation.component.ServletConnector.getInstance().sendRequest("NewGenServlet", xml34);
            System.out.println("xml from servlet is" + samp);
            String from = "";
            org.jdom.input.SAXBuilder sab1 = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc1 = sab1.build(new java.io.StringReader(samp));
            String un = doc1.getRootElement().getText();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Calendar cal1 = java.util.Calendar.getInstance();
            java.util.StringTokenizer st = new java.util.StringTokenizer(un, "-");
            String t[] = new String[3];
            for (int i = 0; st.hasMoreElements(); i++) {
                t[i] = st.nextToken();
                System.out.println("token r" + t[i]);
            }
            Integer y = new Integer(t[0]);
            Integer m = new Integer(t[1]);
            Integer dt = new Integer(t[2]);
            cal1.set(y.intValue(), m.intValue() - 1, dt.intValue());
            if (cal1.get(cal1.DATE) < cal.get(cal.DATE)) {
                dateField1.setText(cal1.get(cal1.YEAR) + "-" + (cal1.get(cal1.MONTH) + 1) + "-" + cal1.get(cal1.DATE) + 1);
            } else {
                dateField1.setText(cal1.get(cal1.YEAR) + "-" + (cal1.get(cal1.MONTH) + 1) + "-" + cal1.get(cal1.DATE));
            }
            dateField2.setText(cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE));
            fr = dateField1.getText();
            String unt = dateField2.getText();
            String urlstr = "http://203.197.20.2:8080/newgenlibctxt/oai2.0?verb=ListRecords&metadataPrefix=marc21&from=" + fr + "&until=" + unt;
            String path = "C:" + java.io.File.separator + "NewGenLibFiles" + java.io.File.separator + "OAIPMH_RECORDS" + java.io.File.separator;
            String path1 = path;
            String path2 = path;
            java.io.File file = new java.io.File(path);
            if (file.isDirectory() == false) {
                file.mkdirs();
            }
            for (int i = 0; resExists == false; i++) {
                try {
                    path = "C:" + java.io.File.separator + "NewGenLibFiles" + java.io.File.separator + "OAIPMH_RECORDS" + java.io.File.separator;
                    java.net.URL url = new java.net.URL(urlstr);
                    java.io.InputStream is = url.openStream();
                    java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                    StringBuffer a = new StringBuffer(10);
                    java.io.File f = new java.io.File("c:" + java.io.File.separator + "NewGenLibFiles" + java.io.File.separator + "OAI_REC" + java.io.File.separator + resT + ".xml");
                    int ch;
                    while ((ch = is.read()) != -1) {
                        a.append((char) ch);
                    }
                    path = path + resT + ".xml";
                    java.io.FileWriter w = new java.io.FileWriter(path);
                    String cont = a.substring(0);
                    w.write(cont);
                    w.close();
                    is.close();
                    org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
                    org.jdom.Document doc = sb.build(path);
                    try {
                        resT = doc.getRootElement().getChild("ListRecords", doc.getRootElement().getNamespace()).getChildText("resumptionToken", doc.getRootElement().getNamespace());
                    } catch (Exception e) {
                    }
                    System.out.println("res is" + resT);
                    if (!(resT == null)) {
                        urlstr = "http://203.197.20.2:8080/newgenlibctxt/oai2.0?verb=ListRecords&from=" + fr + "&resumptionToken=" + resT + "&until=" + unt;
                    } else {
                        resExists = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            path2 = path2 + "library.xml";
            java.io.FileWriter fw = new java.io.FileWriter(path2);
            org.jdom.Element libName = new org.jdom.Element("LibraryName");
            libName.setText(newgen.presentation.NewGenMain.getAppletInstance().getLibraryName(newgen.presentation.NewGenMain.getAppletInstance().getLibraryID()));
            org.jdom.output.XMLOutputter outl = new org.jdom.output.XMLOutputter();
            fw.write(outl.outputString(libName));
            fw.flush();
            System.out.println("lib xml" + outl.outputString(libName));
            unicodeTextField2.setText(zipCompress(path1));
            org.jdom.Element op1 = new org.jdom.Element("OperationId");
            op1.setAttribute("no", "35");
            op1.setAttribute("id", doc1.getRootElement().getAttributeValue("id"));
            op1.setText(fr);
            org.jdom.output.XMLOutputter out1 = new org.jdom.output.XMLOutputter();
            String xml35 = out1.outputString(op1);
            String success = newgen.presentation.component.ServletConnector.getInstance().sendRequest("NewGenServlet", xml35);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
