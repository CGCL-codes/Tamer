    public static InputStream loadURL(final String resource) throws IOException {
        final URL url = urlLoader.findResource(resource);
        if (url == null) throw new FileNotFoundException();
        return url.openStream();
    }
