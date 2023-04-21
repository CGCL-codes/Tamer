    public FileWordReader(URL url) throws IOException {
        this.fileName = url.getPath();
        this.reader = new LineNumberReader(new BufferedReader(new InputStreamReader(url.openStream())));
    }
