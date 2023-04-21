    @Test
    public void test_typeIdToTypeName_NonExistingID() throws Exception {
        URL url = new URL(baseUrl + "/typeIdToTypeName/1234567890");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(400));
    }
