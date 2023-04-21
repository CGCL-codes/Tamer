    public HttpURLConnection connect() throws IOException {
        if (server == null) {
            server = (HttpURLConnection) url.openConnection();
        }
        return server;
    }
