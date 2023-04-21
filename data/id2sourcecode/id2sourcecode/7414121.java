    public Ico(URL url) throws BadIcoResException, IOException {
        this(url.openStream());
    }
