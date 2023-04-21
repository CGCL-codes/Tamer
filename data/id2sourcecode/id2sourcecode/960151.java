    public SearchSRUServer(String url, String startRecord, String maximumRecords, String query, String recordSchema, String serverId, String libraryId, java.util.Hashtable records, String serverName) {
        this.records = records;
        String error = "";
        String operation = "searchRetrieve";
        String recordPacking = "XML";
        String recordData = "XML";
        String version = "1.1";
        url += "?operation=searchRetrieve&recordSchema=" + recordSchema + "&version=" + version + "&query=" + query + "&startRecord=" + startRecord + "&maximumRecords=" + maximumRecords;
        java.net.URL urlJ = null;
        try {
            urlJ = new java.net.URL(url);
        } catch (Exception exp) {
            exp.printStackTrace();
            error = "INVALID_URL";
        }
        if (urlJ != null) {
            java.io.InputStream os = null;
            try {
                java.net.URLConnection urlconn = (java.net.URLConnection) urlJ.openConnection();
                urlconn.setDoOutput(true);
                os = urlconn.getInputStream();
            } catch (Exception exp) {
                exp.printStackTrace();
                error = "CANNOT_CONNECT_TO_SERVER";
            }
            if (os != null) {
                org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = null;
                try {
                    doc = sb.build(os);
                } catch (Exception exp) {
                    exp.printStackTrace();
                    error = "EXPLAIN_XML_INVALID";
                }
                if (doc != null) {
                    org.jdom.Namespace sruns = org.jdom.Namespace.getNamespace("http://www.loc.gov/zing/srw/");
                    java.util.List lirecords = doc.getRootElement().getChild("records", sruns).getChildren("record", sruns);
                    for (int i = 0; i < lirecords.size(); i++) {
                        org.jdom.Element eleRecord = (org.jdom.Element) lirecords.get(i);
                        String schemaSent = eleRecord.getChildTextTrim("recordSchema", sruns);
                        String originStr = ejb.bprocess.opac.RecordSchemas.getInstance().getOriginString(schemaSent);
                        org.jdom.Element recordRootOrig = (org.jdom.Element) eleRecord.getChild("recordData", sruns).getChildren().get(0);
                        org.jdom.Element recordRoot = (new ejb.bprocess.opac.AnySchemaToMARCConverter()).convert((org.jdom.Element) recordRootOrig.clone(), originStr);
                        org.jdom.Namespace marcns = recordRoot.getNamespace();
                        records.put(serverId + "_" + libraryId + "_" + i, getInitialDataFromMarcXML(recordRoot, marcns));
                        java.util.Hashtable htForLib = (java.util.Hashtable) records.get(serverId + "_" + libraryId + "_" + i);
                        htForLib.put("SERVER_NAME", serverName);
                    }
                }
            }
        }
    }
