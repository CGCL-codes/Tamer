    protected Object loadResourceInternal(String aResolvedLocation, String aTypeURI) throws AeException {
        try {
            URL url = new URL(aResolvedLocation);
            InputSource source = new InputSource(url.openStream());
            source.setSystemId(url.toExternalForm());
            return source;
        } catch (Throwable ex) {
            throw new AeException(ex);
        }
    }
