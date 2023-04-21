    public String getContent() throws IOException {
        try {
            if (connection == null) {
                connection = url.openConnection();
                if (connection instanceof HttpURLConnection) responseCode = ((HttpURLConnection) connection).getResponseCode();
            }
            String content = connection.getContent().toString();
            connection = null;
            return content;
        } catch (IOException e) {
            LOG.warn("URL could not be opened: " + e.getMessage(), e);
            return null;
        }
    }
