    @Test
    public void test_admin_tokenizeInvType() throws Exception {
        URL url = new URL(baseUrl + "/admin/tokenizeInvType");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(302));
    }
