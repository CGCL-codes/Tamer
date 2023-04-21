    public void setAllInitialData(java.util.ArrayList al) {
        String reqxml = newgen.presentation.cataloguing.CataloguingXMLGenerator.getInstance().getAuthorityFilesMatchedData("3", al, newgen.presentation.NewGenMain.getAppletInstance().getCataloguingPool());
        try {
            java.net.URL url = new java.net.URL(ResourceBundle.getBundle("Administration").getString("ServerURL") + ResourceBundle.getBundle("Administration").getString("ServletSubPath") + "CatalogueRecordServlet");
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            urlconn.setDoOutput(true);
            java.io.OutputStream dos = urlconn.getOutputStream();
            dos.write(reqxml.getBytes());
            java.io.InputStream ios = urlconn.getInputStream();
            SAXBuilder saxb = new SAXBuilder();
            Document retdoc = saxb.build(ios);
            Element rootelement = retdoc.getRootElement();
            java.util.List list = rootelement.getChildren("Field");
            for (int i = 0; i < list.size(); i++) {
                Vector vsubmatchdata = new Vector(1, 1);
                String[] fielddata = new String[3];
                fielddata[0] = ((Element) list.get(i)).getChild("FieldId").getText();
                fielddata[1] = ((Element) list.get(i)).getChild("Name").getText();
                fielddata[2] = ((Element) list.get(i)).getChild("TableRowNo").getText();
                vsubmatchdata.addElement(fielddata);
                java.util.List lisubmatchlist = ((Element) list.get(i)).getChildren("Record");
                vsubmatchdata.addElement(lisubmatchlist);
                this.vfullmatchdata.addElement(vsubmatchdata);
            }
            Vector vsubmatchdata = (Vector) this.vfullmatchdata.elementAt(0);
            String[] fulldata = (String[]) vsubmatchdata.elementAt(0);
            this.tfAuthorityFileName.setText(this.getAuthorityFileName(fulldata[0]));
            this.tfMatchedTerm.setText(fulldata[1]);
            this.tfFieldNumber.setText(fulldata[0]);
            this.tableMatchesFound.setModel(this.getAuthorityFileTableModel(fulldata[0]));
            this.getAuthorityFileDataforTableModel(fulldata[0], (java.util.List) vsubmatchdata.elementAt(1));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
