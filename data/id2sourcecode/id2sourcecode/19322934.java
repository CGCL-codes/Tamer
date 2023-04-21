    @Test
    public void test_lookupBlueprintType_FullSearch_Trimming() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/gallente+");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":21945,\"itemCategoryID\":9,\"name\":\"Gallente Administrative Outpost Platform Blueprint\",\"icon\":\"06_03\"},{\"itemTypeID\":2780,\"itemCategoryID\":9,\"name\":\"Gallente Control Tower Blueprint\",\"icon\":\"07_15\"},{\"itemTypeID\":2781,\"itemCategoryID\":9,\"name\":\"Gallente Control Tower Medium Blueprint\",\"icon\":\"07_15\"},{\"itemTypeID\":2782,\"itemCategoryID\":9,\"name\":\"Gallente Control Tower Small Blueprint\",\"icon\":\"07_15\"},{\"itemTypeID\":11130,\"itemCategoryID\":9,\"name\":\"Gallente Shuttle Blueprint\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
