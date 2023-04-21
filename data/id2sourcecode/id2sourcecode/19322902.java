    @Test
    public void test_admin_reindex() throws Exception {
        URL url = new URL(baseUrl + "/admin/reindex");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(302));
    }
