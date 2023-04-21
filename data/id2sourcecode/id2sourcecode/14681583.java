    public AImage(URL url) throws IOException {
        this(getName(url), Files.readAll(url.openStream()));
    }
