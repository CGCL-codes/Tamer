    private HttpURLConnection makeConnection() throws IOException {
        String encodedUrl;
        try {
            encodedUrl = URLEncoder.encode(url.toExternalForm(), ENCODING);
        } catch (UnsupportedEncodingException e) {
            encodedUrl = url.toExternalForm();
        }
        try {
            URL url = new URL(IS_GD_API_URL + encodedUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new IOException("Failed to create connection for compressing URL.", e);
        }
    }
