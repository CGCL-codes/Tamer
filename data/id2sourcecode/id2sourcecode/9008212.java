    private HttpURLConnection connect(String urlString) throws IOException {
        System.out.println("Connect to: " + urlString);
        HttpURLConnection httpConn = (HttpURLConnection) (new URL(urlString)).openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("GET");
        httpConn.addRequestProperty("Connection", "Keep-Alive");
        httpConn.addRequestProperty("Content-Type", "text/xml");
        return httpConn;
    }
