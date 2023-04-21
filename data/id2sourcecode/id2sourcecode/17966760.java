    public InputSource resolveEntity(final String publicId, final String systemId) throws IOException, SAXException {
        int i;
        InputSource source = null;
        if (systemId != null && baseUrl != null) {
            URL url;
            try {
                url = new URL(baseUrl, systemId);
                source = new InputSource(url.openStream());
                source.setSystemId(systemId);
                return source;
            } catch (MalformedURLException except) {
                try {
                    String absURL = URIUtils.resolveAsString(systemId, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(systemId);
                    return source;
                } catch (MalformedURLException ex2) {
                }
            } catch (java.io.FileNotFoundException fnfe) {
                try {
                    String absURL = URIUtils.resolveAsString(systemId, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(systemId);
                    return source;
                } catch (MalformedURLException ex2) {
                }
            }
            return null;
        }
        return null;
    }
