    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String resource = schemaMapping.get(systemId);
        URL url = bundle.getResource(resource);
        if (url != null) {
            return new InputSource(url.openStream());
        }
        return null;
    }
