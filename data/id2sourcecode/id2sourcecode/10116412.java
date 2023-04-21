    public URLOutputStream(final URL url) throws IOException {
        this(url.openConnection().getOutputStream());
    }
