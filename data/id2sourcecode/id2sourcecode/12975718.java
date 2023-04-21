    public TidyParser(URL url) throws IOException {
        this.is = url.openStream();
        this.tidy = new Tidy();
        init();
    }
