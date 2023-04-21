    public XMLElement readXML(URL doc_url) {
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = doc_url.openStream();
            try {
                bis = new BufferedInputStream(is);
                try {
                    readXML(bis);
                } catch (Exception ex) {
                    logger.error("Failed to read XML from " + doc_url + ": " + ex, ex);
                    clean();
                }
            } catch (Exception ex) {
                logger.error("caught Exception in BufferedInputStream for " + doc_url + ": " + ex, ex);
                clean();
            }
        } catch (Exception ex) {
            clean();
        }
        return root_element;
    }
