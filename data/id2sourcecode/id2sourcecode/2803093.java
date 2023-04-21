    public static InputStream openPostStream(URL url, byte[] content, String contentType) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        if (contentType != null) {
            conn.setRequestProperty("Content-Type", contentType);
        }
        conn.setRequestProperty("Content-Length", String.valueOf(content.length));
        OutputStream out = conn.getOutputStream();
        try {
            out.write(content);
        } finally {
            out.close();
        }
        return conn.getInputStream();
    }
