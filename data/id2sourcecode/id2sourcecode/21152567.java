    public final void setIs(URL url) throws IOException {
        if (url != null) {
            this.url = url;
            setIs(url.openStream());
        }
    }
