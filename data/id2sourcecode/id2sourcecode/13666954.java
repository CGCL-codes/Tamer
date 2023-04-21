    public static InputStream loadResource(String resource) {
        URL url = FileUtils.class.getResource(resource);
        try {
            return url == null ? null : url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
