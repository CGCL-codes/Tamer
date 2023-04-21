    public EmailResolver(String resourceLoc) {
        URL resource = ClassLoaderUtil.getResource(resourceLoc, getClass());
        if (resource == null) throw new IllegalStateException("resource: " + resourceLoc + " not found");
        try {
            _urls.load(resource.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
