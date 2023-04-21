    public static byte[] doFetchData(String url, String method, String contentType, String ssic, InputStream in) throws IOException {
        HttpURLConnection conn = openConnection(url, method, contentType, ssic);
        if (in != null) sendData(conn, in);
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return fetchData(conn);
        } else {
            throw new IOException("Http response is not OK. code=" + conn.getResponseCode());
        }
    }
