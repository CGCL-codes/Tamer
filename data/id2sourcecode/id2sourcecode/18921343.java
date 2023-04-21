    public Document getLatestXML() {
        Document doc = null;
        try {
            if (conurl != null) doc = getDocument(conurl.openStream(), conurl.toString());
        } catch (Exception e) {
            System.err.println("ERROR: WebWorkBook.getLatestXML: Connecting to: " + ServerURL + ":" + e.toString());
        }
        return doc;
    }
