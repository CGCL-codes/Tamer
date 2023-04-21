    protected HttpURLConnection getConnection(String method, boolean out) throws IOException, ProtocolException {
        System.out.println(method + " " + this.url);
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(out);
        return connection;
    }
