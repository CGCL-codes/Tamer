    @Test
    public void test_tyr101specific_SBU() throws Exception {
        URL url = new URL(baseUrl + "/typeNameToTypeID/Sovereignty+Blockade+Unit+Blueprint");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("2738"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain; charset=utf-8"));
    }
