    public boolean readXML(URL doc_url) {
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = doc_url.openStream();
        } catch (Exception ex) {
            return false;
        }
        try {
            bis = new BufferedInputStream(is);
        } catch (Exception ex) {
            System.err.println("caught Exception in BufferedInputStream for " + doc_url + ": " + ex);
            return false;
        }
        try {
            return (readXML(bis));
        } catch (Exception ex) {
            System.err.println("Failed to read XML from " + doc_url + ": " + ex);
            return false;
        }
    }
