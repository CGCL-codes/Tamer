    private void connServer(String SOAPUrl) throws Exception {
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        httpConn = (HttpURLConnection) connection;
    }
