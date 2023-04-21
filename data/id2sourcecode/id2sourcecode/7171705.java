    public void parse(URL url) throws IOException {
        this.filename = url.toExternalForm();
        this.parse(url.openStream());
    }
