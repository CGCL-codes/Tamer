    public ZipFileSystem(URL zipFile) {
        if (zipFile == null) {
            throw new IllegalArgumentException("zipFile == null");
        }
        final URL furl = zipFile;
        zipfile = new ZipDataSource() {

            public InputStream zipData() throws IOException {
                return furl.openStream();
            }
        };
        refreshZip();
    }
