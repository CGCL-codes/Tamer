    private void assertResponseCode(String url, int response, String mime) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        assertEquals(response, con.getResponseCode());
        assertEquals(mime, con.getHeaderField("Content-Type"));
    }
