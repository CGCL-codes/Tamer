    @Test
    public void test_lookupType_FullSearch_ThreeWords_Issue31() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/warp+Disruptor+II");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":26889,\"itemCategoryID\":9,\"name\":\"Mobile Large Warp Disruptor II Blueprint\",\"icon\":\"06_03\"},{\"itemTypeID\":26891,\"itemCategoryID\":9,\"name\":\"Mobile Medium Warp Disruptor II Blueprint\",\"icon\":\"06_03\"},{\"itemTypeID\":26893,\"itemCategoryID\":9,\"name\":\"Mobile Small Warp Disruptor II Blueprint\",\"icon\":\"06_03\"},{\"itemTypeID\":3245,\"itemCategoryID\":9,\"name\":\"Warp Disruptor II Blueprint\",\"icon\":\"04_09\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
