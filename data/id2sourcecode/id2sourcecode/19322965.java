    @Test
    public void test_lookupType_FullSearch_FourWords_Issue31() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/small+energy+transfer+ii");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":1191,\"itemCategoryID\":9,\"name\":\"Small Energy Transfer Array II Blueprint\",\"icon\":\"01_02\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
