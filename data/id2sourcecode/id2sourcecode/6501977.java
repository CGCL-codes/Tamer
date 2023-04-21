    public DocumentFragmentInputSource(String systemId) throws IOException {
        InputStream urlStream = new URL(systemId).openStream();
        this.setByteStream(new AddRootElementInputStream(urlStream));
        this.setSystemId(systemId);
    }
