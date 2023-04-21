    @Test
    public void test_typeIdToTypeName() throws Exception {
        URL url = new URL(baseUrl + "/typeIdToTypeName/20187");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("Obelisk"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain; charset=utf-8"));
    }
