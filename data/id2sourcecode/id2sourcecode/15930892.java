    public void testCallIndexPage() throws Exception {
        URL url = new URL("http://localhost:8080/cargo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
    }
