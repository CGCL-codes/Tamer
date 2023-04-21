    public static InputStream getFile(String filename) throws IOException {
        URL url;
        url = Platform.getBundle(DenguesCommonsPlugin.PLUGIN_ID).getEntry(filename);
        return url != null ? url.openStream() : null;
    }
