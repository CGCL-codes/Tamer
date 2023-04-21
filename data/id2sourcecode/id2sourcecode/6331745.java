    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (BootstrapResolver.xmlCatalogPubId.equals(publicId) || BootstrapResolver.xmlCatalogSysId.equals(systemId)) {
            URL url = BootstrapResolver.class.getResource("/org/apache/xml/resolver/etc/catalog.dtd");
            if (url != null) {
                InputSource in = new InputSource(url.toString());
                in.setByteStream(url.openStream());
                in.setPublicId(publicId);
                return in;
            }
        }
        if (entityResolver != null) return entityResolver.resolveEntity(publicId, systemId);
        return null;
    }
