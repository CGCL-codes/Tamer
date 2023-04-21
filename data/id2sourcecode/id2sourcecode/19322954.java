    @Test
    public void test_version() throws Exception {
        URL url = new URL(baseUrl + "/version");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertTrue(getResponse(connection).endsWith("-cru16"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/html"));
    }
