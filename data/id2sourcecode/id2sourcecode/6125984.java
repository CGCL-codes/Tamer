    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (SCHEMA_URI.equals(systemId)) {
            URL url = this.getClass().getClassLoader().getResource(SCHEMA_CLASSPATH);
            if (url != null) {
                return new InputSource(url.openStream());
            }
        }
        return null;
    }
