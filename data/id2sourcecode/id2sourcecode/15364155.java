    private void storeConfigurationXMLFile(java.net.URL url, String comp) {
        java.util.Properties p;
        try {
            p = new java.util.Properties();
            p.loadFromXML(url.openStream());
        } catch (java.io.IOException ie) {
            System.err.println("error opening: " + url.getPath() + ": " + ie.getMessage());
            return;
        }
        storeConfiguration(p, comp);
        return;
    }
