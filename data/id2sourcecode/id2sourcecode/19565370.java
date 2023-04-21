    public Object loadFromXML(URL url) {
        Logger.getLogger(XMLHandler.class.getName()).debug("Loading XML from URL: " + url.toString());
        InputStream is = null;
        Object rv = null;
        try {
            rv = loadFromXML(is = url.openStream());
        } catch (IOException ex) {
            Logger.getLogger(XMLHandler.class.getName()).error("Error when loading from URL: " + url.toString(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLHandler.class.getName()).error("Cannot close output stream of the URL", ex);
                }
            }
        }
        return rv;
    }
