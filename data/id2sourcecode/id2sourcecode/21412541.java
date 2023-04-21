    public void setUrl(final URL newUrl) {
        this.url = newUrl;
        this.properties = new Properties();
        try {
            final InputStream stream = this.url.openStream();
            this.properties.load(stream);
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
