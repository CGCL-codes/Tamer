    public Map parseCatalog(String catalogURI) {
        URL url = null;
        Map map = null;
        try {
            url = FSCatalog.class.getClassLoader().getResource(catalogURI);
            map = parseCatalog(new InputSource(new BufferedInputStream(url.openStream())));
        } catch (Exception ex) {
            XRLog.xmlEntities(Level.WARNING, "Could not open XML catalog from URI '" + catalogURI + "'", ex);
            map = new HashMap();
        }
        return map;
    }
