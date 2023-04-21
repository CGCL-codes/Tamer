    public static Collection readXML(Bundle declaringBundle, URL url) throws IllegalXMLException {
        try {
            return readXML(declaringBundle, url.openStream());
        } catch (IOException e) {
            throw new IllegalXMLException("Could not open \"" + url + "\" got exception.", e);
        }
    }
