    @Test
    public void test_typeNameToTypeID() throws Exception {
        URL url = new URL(baseUrl + "/typeNameToTypeID/Obelisk");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("20187"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain; charset=utf-8"));
        url = new URL(baseUrl + "/typeNameToTypeID/Obelisk+Blueprint");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("20188"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain; charset=utf-8"));
        url = new URL(baseUrl + "/typeNameToTypeID/Obelisk%20Blueprint");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("20188"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("text/plain; charset=utf-8"));
        url = new URL(baseUrl + "/typeNameToTypeID/Obelisk Blueprint");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/plain");
        assertThat(connection.getResponseCode(), equalTo(400));
    }
