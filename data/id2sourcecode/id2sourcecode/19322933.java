    @Test
    public void test_lookupBlueprintType_FullSearch_TwoWordsInMiddle() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/drone+control");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":24284,\"itemCategoryID\":9,\"name\":\"Drone Control Unit I Blueprint\",\"icon\":\"03_06\"},{\"itemTypeID\":25909,\"itemCategoryID\":9,\"name\":\"Large Drone Control Range Augmentor I Blueprint\",\"icon\":\"02_10\"},{\"itemTypeID\":26325,\"itemCategoryID\":9,\"name\":\"Large Drone Control Range Augmentor II Blueprint\",\"icon\":\"02_10\"},{\"itemTypeID\":32028,\"itemCategoryID\":9,\"name\":\"Medium Drone Control Range Augmentor I Blueprint\",\"icon\":\"02_10\"},{\"itemTypeID\":32032,\"itemCategoryID\":9,\"name\":\"Medium Drone Control Range Augmentor II Blueprint\",\"icon\":\"02_10\"},{\"itemTypeID\":32026,\"itemCategoryID\":9,\"name\":\"Small Drone Control Range Augmentor I Blueprint\",\"icon\":\"02_10\"},{\"itemTypeID\":32030,\"itemCategoryID\":9,\"name\":\"Small Drone Control Range Augmentor II Blueprint\",\"icon\":\"02_10\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
    }
