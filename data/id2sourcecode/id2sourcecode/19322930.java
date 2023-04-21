    @Test
    public void test_lookupBlueprintType_FullSearch_CaseSensivity() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/meGAt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":995,\"itemCategoryID\":9,\"name\":\"Megathron Blueprint\"},{\"itemTypeID\":13203,\"itemCategoryID\":9,\"name\":\"Megathron Federate Issue Blueprint\"},{\"itemTypeID\":17729,\"itemCategoryID\":9,\"name\":\"Megathron Navy Issue Blueprint\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
