    @Test
    public void test_admin_clearCache() throws Exception {
        URL url = new URL(baseUrl + "/admin/clearCache");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(302));
    }
