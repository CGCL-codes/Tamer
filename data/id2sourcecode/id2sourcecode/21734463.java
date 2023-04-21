    private String getMimeType(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            return connection.getContentType();
        } catch (IOException e) {
            return null;
        }
    }
