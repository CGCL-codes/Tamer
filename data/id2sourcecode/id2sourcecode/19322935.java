    @Test
    public void test_lookupResourceType() throws Exception {
        URL url = new URL(baseUrl + "/lookupResourceType/Tri");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":25595,\"itemCategoryID\":4,\"name\":\"Alloyed Tritanium Bar\",\"icon\":\"69_11\"},{\"itemTypeID\":25593,\"itemCategoryID\":4,\"name\":\"Smashed Trigger Unit\",\"icon\":\"69_13\"},{\"itemTypeID\":25612,\"itemCategoryID\":4,\"name\":\"Trigger Unit\",\"icon\":\"69_14\"},{\"itemTypeID\":25598,\"itemCategoryID\":4,\"name\":\"Tripped Power Circuit\",\"icon\":\"69_15\"},{\"itemTypeID\":34,\"itemCategoryID\":4,\"name\":\"Tritanium\",\"icon\":\"06_14\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><rowset><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_11</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25595</itemTypeID><name>Alloyed Tritanium Bar</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_13</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25593</itemTypeID><name>Smashed Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_14</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25612</itemTypeID><name>Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_15</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25598</itemTypeID><name>Tripped Power Circuit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>06_14</icon><itemCategoryID>4</itemCategoryID><itemTypeID>34</itemTypeID><name>Tritanium</name></row></rowset>"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/xml; charset=utf-8"));
    }
