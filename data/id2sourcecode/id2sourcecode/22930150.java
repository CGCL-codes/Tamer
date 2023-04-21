    public InputStream getInputStream() throws IOException {
        try {
            if (connection == null) {
                connection = url.openConnection();
                if (connection instanceof HttpURLConnection) responseCode = ((HttpURLConnection) connection).getResponseCode();
            }
            InputStream is = null;
            if (responseCode != HttpURLConnection.HTTP_NOT_FOUND) is = connection.getInputStream();
            connection = null;
            return is;
        } catch (ConnectException e) {
            LOG.warn("Unable to connect to URL: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            LOG.warn("URL could not be opened: " + e.getMessage(), e);
            throw e;
        }
    }
