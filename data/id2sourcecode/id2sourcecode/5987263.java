    public void testGetForm() throws IOException {
        URL url = new URL("http://localhost:8282/message");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        assertEquals(200, con.getResponseCode());
    }
