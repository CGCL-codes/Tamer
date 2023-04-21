    private HttpURLConnection connect(String urlString) throws MalformedURLException, IOException {
        HttpURLConnection httpConn = (HttpURLConnection) (new URL(urlString)).openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.addRequestProperty("Connection", "Keep-Alive");
        httpConn.addRequestProperty("Content-Type", "text/xml");
        return httpConn;
    }
