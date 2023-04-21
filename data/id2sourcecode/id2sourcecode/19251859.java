    protected String readFile(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setRequestProperty("Content-Type", "text/html");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        httpURLConnection.getResponseCode();
        byte[] byteArray = consumeResponse(httpURLConnection.getInputStream());
        return new String(byteArray);
    }
