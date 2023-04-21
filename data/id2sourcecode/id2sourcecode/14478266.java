    public static <D> D read(URL url, Class<D> descriptor) {
        try {
            return read(url.openStream(), descriptor);
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
