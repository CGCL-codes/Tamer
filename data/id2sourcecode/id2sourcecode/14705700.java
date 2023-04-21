    public void resolve(Resource resource, Context context) throws IOException {
        URL url = getResource(resource.getPath());
        if (url == null) throw new IOException(resource.getPath() + " not found in classpath.");
        resource.resolve(newReader(url.openStream()), getType());
    }
