    private void assertUrlContent(URL url, byte[] expectedContent) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();
        assertStreamContent(expectedContent, stream);
    }
