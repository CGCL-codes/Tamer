    public Connector(String url) throws IOException {
        this.url = new URL(url);
        this.connection = this.url.openConnection();
        this.responseMap = this.connection.getHeaderFields();
    }
