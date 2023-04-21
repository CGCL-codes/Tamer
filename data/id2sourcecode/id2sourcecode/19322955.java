    @Test
    public void test_root() throws Exception {
        URL url = new URL(baseUrl + "/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertTrue(getResponse(connection).endsWith("</html>"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/html"));
        url = new URL(baseUrl + "/robots.txt");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertTrue(getResponse(connection).endsWith("Disallow: /"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain"));
        url = new URL(baseUrl + "/favicon.ico");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("image/x-icon"));
    }
