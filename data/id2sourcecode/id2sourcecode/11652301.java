    public void setUrl(final URL url) throws IOException {
        this.url = url;
        this.is = url.openStream();
    }
