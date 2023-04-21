    public FIXFileBrowserAction(URL url, int maxMessages) throws IOException {
        super();
        this.istream = url.openStream();
        this.title = url.toString();
    }
