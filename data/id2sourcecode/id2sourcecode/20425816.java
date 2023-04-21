    @Override
    protected FsUrlConnection openConnection(URL url) throws IOException {
        return new FsUrlConnection(conf, url);
    }
