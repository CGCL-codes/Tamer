    public void fillTableValues() {
        java.util.ArrayList cpool = newgen.presentation.NewGenMain.getAppletInstance().getCataloguingPool();
        String xmlreq = AdministrationXMLGenerator.getInstance().getPoolFormSubDivision("6", cpool);
        System.out.println(xmlreq);
        try {
            java.net.URL url = new java.net.URL(ResourceBundle.getBundle("Administration").getString("ServerURL") + ResourceBundle.getBundle("Administration").getString("ServletSubPath") + "SubDivisionServlet");
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setDoOutput(true);
            java.io.OutputStream dos = urlconn.getOutputStream();
            dos.write(xmlreq.getBytes());
            java.io.InputStream ios = urlconn.getInputStream();
            SAXBuilder saxb = new SAXBuilder();
            Document retdoc = saxb.build(ios);
            Element rootelement = retdoc.getRootElement();
            java.util.List onelist = rootelement.getChildren();
            for (int i = 0; i < onelist.size(); i++) {
                Element rec = (Element) onelist.get(i);
                Object[] r = new Object[1];
                String[] formlib = new String[2];
                formlib[0] = rec.getChild("FormSubDivisionId").getText();
                formlib[1] = rec.getChild("LibraryId").getText();
                this.formid_libid.add(formlib);
                r[0] = rec.getChild("Name").getText();
                this.dtmSearch.addRow(r);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
