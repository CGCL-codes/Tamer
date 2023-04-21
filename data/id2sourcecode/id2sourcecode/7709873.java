    public void fillTableValues() {
        java.util.ArrayList cpool = newgen.presentation.NewGenMain.getAppletInstance().getCataloguingPool();
        String xmlreq = AdministrationXMLGenerator.getInstance().getPoolGeographicalSubDivision("8", cpool);
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
                String[] geolib = new String[2];
                geolib[0] = rec.getChild("GeographicalSubDivisionId").getText();
                geolib[1] = rec.getChild("LibraryId").getText();
                this.geoid_libid.add(geolib);
                r[0] = rec.getChild("GeographicalSubDivision").getText();
                this.dtmSearch.addRow(r);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
