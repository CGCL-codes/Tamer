    @Override
    protected String requestToString(final String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            return IOUtil.toString(connection.getInputStream());
        } finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
    }
