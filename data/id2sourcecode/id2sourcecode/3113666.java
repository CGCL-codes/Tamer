    protected InputStream openFile(final String url) throws IOException {
        return AURLUtil.toURL_notNull(url).openStream();
    }
