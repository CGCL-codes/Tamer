    public static Document loadXMLFromURL(URL url) {
        try {
            return loadXMLFromStream(url.openStream(), true);
        } catch (IOException ex) {
            Assert.fail(ex);
        }
        return null;
    }
