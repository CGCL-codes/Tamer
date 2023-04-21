    private void assertUrlErrorStreamContent(URL url, byte[] expectedContent) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream stream = connection.getErrorStream();
        assertStreamContent(expectedContent, stream);
    }
