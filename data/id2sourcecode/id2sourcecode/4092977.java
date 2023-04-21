    public static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader loader = ResourceStreamProvider.getLoader();
        URL url = null;
        if (loader == null) {
            url = Logger.class.getResource(resource);
        } else {
            url = loader.getResource(resource);
            if (url == null) {
                url = Logger.class.getResource(resource);
            }
        }
        if (url == null) {
            throw new IOException("Missing resource: " + resource);
        }
        String protocol = url.getProtocol();
        if (forbiddenProtocols.contains(protocol)) {
            throw new IOException("Wrong protocol [" + protocol + "] for resource : " + resource);
        }
        return url.openStream();
    }
