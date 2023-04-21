    protected BufferedInputStream getBufferedInputStream(String path) {
        URL url = ResourceUtil.getResource(path);
        return new BufferedInputStream(URLUtil.openStream(url));
    }
