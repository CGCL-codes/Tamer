    public URLInputStream(final URL url) throws IOException {
        this(url.openConnection().getInputStream());
    }
