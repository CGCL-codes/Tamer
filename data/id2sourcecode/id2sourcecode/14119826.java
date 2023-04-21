    public HttpURLConnection openConnection() throws IOException {
        httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setDoInput(true);
        httpCon.setRequestProperty("connection", "Keep-Alive");
        return httpCon;
    }
