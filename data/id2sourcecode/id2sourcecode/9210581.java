    UrlInput(String url) throws Exception {
        super(new java.net.URL(url).openStream());
        this.url = url;
    }
