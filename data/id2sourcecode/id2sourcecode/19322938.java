    @Test
    public void test_lookupResourceType_FullSearch_CaseSensivity() throws Exception {
        URL url = new URL(baseUrl + "/lookupResourceType/tRIta");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":25595,\"itemCategoryID\":4,\"name\":\"Alloyed Tritanium Bar\",\"icon\":\"69_11\"},{\"itemTypeID\":34,\"itemCategoryID\":4,\"name\":\"Tritanium\",\"icon\":\"06_14\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
