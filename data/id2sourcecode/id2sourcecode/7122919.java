    public static InputStream getClassAsStream(@Nonnull Class<?> clazz) throws IOException {
        String className = clazz.getName();
        String path = getRelativeClassFilePath(className);
        URL url = clazz.getResource('/' + path);
        return url.openStream();
    }
