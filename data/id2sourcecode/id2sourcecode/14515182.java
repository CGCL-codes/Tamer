    protected static Document getDocument(URL url) throws DeploymentException {
        try {
            InputSource source = new InputSource(url.openStream());
            return getDocument(source, url.toString());
        } catch (IOException e) {
            throw new DeploymentException(e.getMessage());
        }
    }
