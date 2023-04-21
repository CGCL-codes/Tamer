    @Test
    public void test_typeIdToTypeName_StringInsteadOfID() throws Exception {
        URL url = new URL(baseUrl + "/typeIdToTypeName/blah-blah");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(400));
    }
