    public static Document parse(URL url) throws ApplicationException {
        InputStream is = null;
        try {
            is = url.openStream();
            return createDocumentBuilder().parse(is);
        } catch (SAXException e) {
            throw new ApplicationException("Failed to parse the xmlf document", e);
        } catch (IOException e) {
            throw new ApplicationException("Failed to parse the xml document", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.warn("Error closing stream of URL " + url, e);
                }
            }
        }
    }
