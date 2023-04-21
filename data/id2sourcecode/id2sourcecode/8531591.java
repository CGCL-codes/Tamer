    public Document getLatestPingsAsRDF() throws SimalException {
        URL url;
        try {
            url = new URL("http://pingthesemanticweb.com/export/");
        } catch (MalformedURLException e) {
            throw new SimalException("The PTSW URL is malformed, how can that happen since it is hard coded?", e);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = null;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(url.openStream());
        } catch (Exception e) {
            throw new SimalException("Unable to retrive the PTSW export file", e);
        }
        return getPingsAsRDF(doc);
    }
