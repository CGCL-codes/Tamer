    URLTemplateSource(URL url) throws IOException {
        this.url = url;
        this.conn = url.openConnection();
    }
