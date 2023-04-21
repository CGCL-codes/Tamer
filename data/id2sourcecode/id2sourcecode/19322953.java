    @Test
    public void test_typeNameToTypeID_NonExistingName() throws Exception {
        URL url = new URL(baseUrl + "/typeNameToTypeID/blah-blah");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(400));
    }
