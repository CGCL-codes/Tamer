    @Test
    public void test_admin_calculateBlueprintDetails() throws Exception {
        URL url = new URL(baseUrl + "/admin/calculateBlueprintDetails");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertThat(connection.getResponseCode(), equalTo(302));
    }
