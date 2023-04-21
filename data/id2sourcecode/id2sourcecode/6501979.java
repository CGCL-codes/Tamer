    public DocumentFragmentInputSource(String systemId, String namespacePrefix, Map namespaces) throws IOException {
        InputStream urlStream = new URL(systemId).openStream();
        this.setByteStream(new AddRootElementInputStream(urlStream, namespacePrefix, namespaces));
        this.setSystemId(systemId);
    }
