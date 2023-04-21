    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        URL url = new URL(_root, systemId.replaceFirst("^/+", ""));
        InputStream in = url.openStream();
        InputSource source = new InputSource(in);
        source.setPublicId(publicId);
        source.setSystemId(url.toString());
        return source;
    }
