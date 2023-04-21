    @SuppressWarnings("unchecked")
    public static <T> void write(T resource, URL url) throws ResourceException {
        try {
            write((Class<T>) resource.getClass(), resource, url.openConnection().getOutputStream());
            cache.put(url, resource);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
