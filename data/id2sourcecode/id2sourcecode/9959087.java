    public static InputStream post(URL url, InputStream dataStream, String username, String password, int bufSize) throws IOException {
        if (url == null || dataStream == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
        if (bufSize < 1) {
            throw new IllegalArgumentException("Cannot use zero or negative buffer size.");
        }
        if (!"http".equals(url.getProtocol())) {
            throw new IllegalArgumentException("Cannot use non-HTTP URLs.");
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        try {
            conn.setRequestMethod("POST");
        } catch (java.net.ProtocolException e) {
            throw new IllegalStateException("Could not set a HttpURLConnection's method to POST.");
        }
        if (username != null && password != null) {
            byte[] authBytes = (username + ":" + password).getBytes();
            String authString = new String(Base64.encodeBase64(authBytes));
            conn.setRequestProperty("Authorization", "Basic " + authString);
        }
        OutputStream ostream = conn.getOutputStream();
        byte[] b = new byte[bufSize];
        int bytesRead = dataStream.read(b, 0, bufSize);
        while (bytesRead > 0) {
            ostream.write(b, 0, bytesRead);
            bytesRead = dataStream.read(b, 0, bufSize);
        }
        ostream.close();
        return (conn.getInputStream());
    }
