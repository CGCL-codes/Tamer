    private static InputStream openHttpByteRange(URL url, long offset, long len) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String rangeSpec = "bytes=" + offset + "-";
        if (len != -1) {
            long lastByteOffset = offset + len - 1;
            rangeSpec += lastByteOffset;
        }
        conn.setRequestProperty("Range", rangeSpec);
        conn.connect();
        InputStream is = conn.getInputStream();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
            System.err.println("HTTP server does not support Range request headers");
            is.close();
            return null;
        }
        return is;
    }
