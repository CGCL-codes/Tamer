    public void testTest() throws IOException {
        URL url = new URL("http://www.google.com");
        URLConnection connection = url.openConnection();
        HttpURLConnection urlConnection = (HttpURLConnection) connection;
        System.out.println("urlConnection.getResponseCode() = " + urlConnection.getResponseCode());
    }
