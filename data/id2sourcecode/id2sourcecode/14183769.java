    public InputStream getStream(String _path) {
        InputStream r = null;
        URL url = getURL(_path);
        if (url != null) {
            try {
                r = url.openStream();
            } catch (IOException ex) {
            }
        }
        return r;
    }
