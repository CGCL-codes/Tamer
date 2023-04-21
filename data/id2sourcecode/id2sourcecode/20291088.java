    public InputStream getData() throws Exception {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        prepareConnection(connection);
        addCookies(connection);
        System.out.println("Encoding: " + connection.getContentEncoding());
        return connection.getInputStream();
    }
