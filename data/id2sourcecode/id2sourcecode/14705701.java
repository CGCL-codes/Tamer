    public Resource createResource(String path) throws IOException {
        URL url = getResource(path);
        if (url == null) throw new IOException(path + " not found in classpath.");
        return new Resource(path, getType(), newReader(url.openStream()));
    }
