    public static String downloadString(URL url, Proxy proxy, int readTimeout, int socketTimeout) {
        URLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = proxy == null ? url.openConnection() : url.openConnection(proxy);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(socketTimeout);
            connection.connect();
            inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream);
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
            }
            if (connection != null && connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }
