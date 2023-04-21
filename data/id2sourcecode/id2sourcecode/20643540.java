    protected void openInputSourceStream(XMLInputSource source) throws IOException {
        if (source.getCharacterStream() != null) {
            return;
        }
        InputStream stream = source.getByteStream();
        if (stream == null) {
            String systemId = source.getSystemId();
            try {
                URL url = new URL(systemId);
                stream = url.openStream();
            } catch (MalformedURLException e) {
                stream = new FileInputStream(systemId);
            }
            source.setByteStream(stream);
        }
    }
