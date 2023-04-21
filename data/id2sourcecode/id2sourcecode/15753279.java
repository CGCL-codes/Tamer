    public static DataInputStream getResourceOverHTTP(URL url, String mimeType) throws IOException {
        HttpURLConnection connection = null;
        IOException ioException = null;
        DataInputStream is = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(HTTPHDR_ACCEPT, mimeType);
            connection.setRequestProperty(HTTPHDR_USER_AGENT, HTTPHDR_USER_AGENT_VALUE);
            connection.setRequestProperty(HTTPHDR_CONNECTION, HTTPHDR_CONNECTION_CLOSE);
            connection.setRequestProperty(HTTPHDR_CACHE_CONTROL, HTTPHDR_CACHE_CONTROL_NOTRANSFORM);
            connection.setRequestProperty(HTTPHDR_CONTENT_LEN, String.valueOf("0"));
            int rc = connection.getResponseCode();
            if (rc != HttpURLConnection.HTTP_OK) {
                ioException = new IOException("Http Error, response Code is " + rc);
                throw ioException;
            }
            is = new DataInputStream(connection.getInputStream());
        } catch (IOException ioe) {
            throw ioe;
        } finally {
        }
        return is;
    }
