    public Reader getReader() throws IOException {
        try {
            if (connection == null) {
                connection = url.openConnection();
                if (connection instanceof HttpURLConnection) responseCode = ((HttpURLConnection) connection).getResponseCode();
            }
            Reader reader = null;
            if (responseCode != HttpURLConnection.HTTP_NOT_FOUND) reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            connection = null;
            return reader;
        } catch (IOException e) {
            LOG.warn("URL could not be opened: " + e.getMessage(), e);
            throw e;
        }
    }
