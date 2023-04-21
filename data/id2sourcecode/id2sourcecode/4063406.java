    public void addRDFXML(URL url, String baseURI) throws SimalRepositoryException {
        try {
            model.read(url.openStream(), baseURI);
            LOGGER.debug("Added RDF/XML from " + url.toString());
        } catch (IOException e) {
            throw new SimalRepositoryException("Unable to open stream for " + url, e);
        }
    }
