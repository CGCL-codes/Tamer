    @Test
    public void test_extraMaterialsByTypeName_NonExistingName() throws Exception {
        URL url = new URL(baseUrl + "/extraMaterialsForTypeName/blah-blah");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(400));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        assertThat(connection.getResponseCode(), equalTo(400));
    }
