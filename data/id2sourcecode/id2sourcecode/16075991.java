    protected int sendMessage(URL url, String data) throws IOException {
        HttpURLConnection conn = openConnection(url, getBasicAuthUsername(), getBasicAuthPassword());
        conn.setRequestMethod("POST");
        conn.setAllowUserInteraction(false);
        conn.setDoOutput(true);
        conn.setConnectTimeout(getHttpTimeOutInMs());
        conn.setReadTimeout(getHttpTimeOutInMs());
        OutputStream os = conn.getOutputStream();
        try {
            writeMessage(os, data);
            return conn.getResponseCode();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
