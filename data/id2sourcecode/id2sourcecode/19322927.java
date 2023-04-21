    @Test
    public void test_lookupBlueprintType() throws Exception {
        URL url = new URL(baseUrl + "/lookupBlueprintType/Mega");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":23849,\"itemCategoryID\":9,\"name\":\"\\u0027Catapult\\u0027 Mega Beam Laser I Blueprint\",\"icon\":\"13_11\"},{\"itemTypeID\":23847,\"itemCategoryID\":9,\"name\":\"\\u0027Halberd\\u0027 Mega Pulse Laser I Blueprint\",\"icon\":\"13_15\"},{\"itemTypeID\":3547,\"itemCategoryID\":9,\"name\":\"Limited Mega Ion Siege Blaster I Blueprint\",\"icon\":\"13_03\"},{\"itemTypeID\":847,\"itemCategoryID\":9,\"name\":\"Mega Beam Laser I Blueprint\",\"icon\":\"13_11\"},{\"itemTypeID\":3050,\"itemCategoryID\":9,\"name\":\"Mega Beam Laser II Blueprint\",\"icon\":\"13_11\"},{\"itemTypeID\":30224,\"itemCategoryID\":9,\"name\":\"Mega Module Blueprint\",\"icon\":\"01_08\"},{\"itemTypeID\":846,\"itemCategoryID\":9,\"name\":\"Mega Pulse Laser I Blueprint\",\"icon\":\"13_15\"},{\"itemTypeID\":3058,\"itemCategoryID\":9,\"name\":\"Mega Pulse Laser II Blueprint\",\"icon\":\"13_15\"},{\"itemTypeID\":995,\"itemCategoryID\":9,\"name\":\"Megathron Blueprint\"},{\"itemTypeID\":13203,\"itemCategoryID\":9,\"name\":\"Megathron Federate Issue Blueprint\"},{\"itemTypeID\":17729,\"itemCategoryID\":9,\"name\":\"Megathron Navy Issue Blueprint\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><rowset><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_11</icon><itemCategoryID>9</itemCategoryID><itemTypeID>23849</itemTypeID><name>'Catapult' Mega Beam Laser I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_15</icon><itemCategoryID>9</itemCategoryID><itemTypeID>23847</itemTypeID><name>'Halberd' Mega Pulse Laser I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_03</icon><itemCategoryID>9</itemCategoryID><itemTypeID>3547</itemTypeID><name>Limited Mega Ion Siege Blaster I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_11</icon><itemCategoryID>9</itemCategoryID><itemTypeID>847</itemTypeID><name>Mega Beam Laser I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_11</icon><itemCategoryID>9</itemCategoryID><itemTypeID>3050</itemTypeID><name>Mega Beam Laser II Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_08</icon><itemCategoryID>9</itemCategoryID><itemTypeID>30224</itemTypeID><name>Mega Module Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_15</icon><itemCategoryID>9</itemCategoryID><itemTypeID>846</itemTypeID><name>Mega Pulse Laser I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>13_15</icon><itemCategoryID>9</itemCategoryID><itemTypeID>3058</itemTypeID><name>Mega Pulse Laser II Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>9</itemCategoryID><itemTypeID>995</itemTypeID><name>Megathron Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>9</itemCategoryID><itemTypeID>13203</itemTypeID><name>Megathron Federate Issue Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>9</itemCategoryID><itemTypeID>17729</itemTypeID><name>Megathron Navy Issue Blueprint</name></row></rowset>"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/xml; charset=utf-8"));
    }