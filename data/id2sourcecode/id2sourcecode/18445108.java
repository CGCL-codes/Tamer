    public InputStream getInputStream(URL url) throws TestingEngineResponseException {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new TestingEngineResponseException(e);
        }
    }
