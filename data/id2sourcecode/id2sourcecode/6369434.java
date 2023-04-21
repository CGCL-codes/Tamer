    public Object unmarshal(URL url) throws JAXBException {
        try {
            return unmarshal(url.openStream());
        } catch (IOException e) {
            throw new JAXBException(e);
        }
    }
