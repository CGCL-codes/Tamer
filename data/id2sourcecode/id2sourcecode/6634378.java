    public static byte[] getContentsFromURL(String u, int bufsize) throws IOException {
        URL url = new URL(u);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            return IOUtils.toBytes(connection.getInputStream());
        } finally {
            HttpUtils.disconnect(connection);
        }
    }
