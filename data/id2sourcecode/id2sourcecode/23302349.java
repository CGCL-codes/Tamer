    protected HttpURLConnection getHttpUrlConnection(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(readTimeOut);
        con.setConnectTimeout(connectTimeOut);
        return con;
    }
