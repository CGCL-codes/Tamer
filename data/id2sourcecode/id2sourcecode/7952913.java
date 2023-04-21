    public static <T> T read(Class<T> clasz, URL url) throws ResourceException {
        try {
            Object resource = cache.get(url);
            if (resource == null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    T resourceT = read(clasz, stream);
                    cache.put(url, resourceT);
                    return resourceT;
                }
            }
            if (resource != null) {
                if (clasz.isAssignableFrom(resource.getClass())) return clasz.cast(resource);
                throw new ResourceException("Invalid class for resource: '" + url + "'.  Requested: " + clasz.getName() + ".  Returned: " + resource.getClass().getName());
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
        throw new ResourceException("Error reading resource: " + url);
    }
