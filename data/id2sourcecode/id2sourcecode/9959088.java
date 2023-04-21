    public static InputStream post(URL url, Reader dataReader, String username, String password, int bufSize) throws IOException {
        if (url == null || dataReader == null) {
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
        conn.setRequestProperty("Content-type", "text/xml; charset=\"utf-8\"");
        OutputStreamWriter ostream = new OutputStreamWriter(conn.getOutputStream(), "US-ASCII");
        Category log = Logger.getLogger("POSTDATALOG");
        if (log.isDebugEnabled()) {
            String nl = System.getProperty("line.separator");
            log.debug(nl + "HTTP Post: Current time: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.GregorianCalendar().getTime()));
            log.debug(nl + "Data posted:" + nl);
        }
        char[] b = new char[bufSize];
        int bytesRead = dataReader.read(b, 0, bufSize);
        if (bytesRead > 0 && log.isDebugEnabled()) log.debug(new String(b, 0, bytesRead));
        while (bytesRead > 0) {
            ostream.write(b, 0, bytesRead);
            bytesRead = dataReader.read(b, 0, bufSize);
            if (bytesRead > 0 && log.isDebugEnabled()) log.debug(new String(b, 0, bytesRead));
        }
        ostream.close();
        return (conn.getInputStream());
    }
