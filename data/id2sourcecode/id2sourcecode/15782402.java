    public InputStream loadResource(final String uri) throws ResourceException {
        try {
            if (uri.startsWith(RESOURCE_PREFIX)) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                URL url = cl.getResource(uri.substring(RESOURCE_PREFIX.length()));
                if (url == null) {
                    throw new ResourceException("Resource with URL '" + uri + "' not found by ClassLoader " + cl.toString());
                }
                return url.openStream();
            } else {
                return new FileInputStream(uri);
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
