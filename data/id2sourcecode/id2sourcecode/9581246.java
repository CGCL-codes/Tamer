    @Test
    public void mockURLAndHttpURLConnectionWithDynamicMockAndLocalMockField() throws Exception {
        final URL url = new URL("http://nowhere");
        new NonStrictExpectations(url) {

            HttpURLConnection mockHttpConnection;

            {
                url.openConnection();
                result = mockHttpConnection;
                mockHttpConnection.getOutputStream();
                result = new ByteArrayOutputStream();
            }
        };
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        OutputStream out = conn.getOutputStream();
        assertNotNull(out);
        new Verifications() {

            HttpURLConnection mockHttpConnection;

            {
                mockHttpConnection.setDoOutput(true);
                mockHttpConnection.setRequestMethod("PUT");
            }
        };
    }
