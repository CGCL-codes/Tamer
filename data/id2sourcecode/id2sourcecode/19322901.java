    @Test
    public void test_admin_remoteApi() throws Exception {
        URL url = new URL(baseUrl + "/remote_api");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(302));
    }
