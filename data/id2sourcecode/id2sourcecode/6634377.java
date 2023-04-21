    public static String getStringContentsFromURL(String u, String charset) throws URISyntaxException, IOException {
        URL url = new URL(u);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            return IOUtils.toString(connection.getInputStream(), charset);
        } finally {
            HttpUtils.disconnect(connection);
        }
    }
