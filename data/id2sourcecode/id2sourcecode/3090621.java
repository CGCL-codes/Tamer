    private InputStream getInputStream(final String resource) throws IOException {
        URL url = ResourceUtils.getAsUrl(resource);
        if (url != null) return url.openStream();
        return null;
    }
