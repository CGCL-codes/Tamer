    private static void doPut(LNISoapServlet lni, String collHandle, String packager, String source, String endpoint) throws java.rmi.RemoteException, ProtocolException, IOException, FileNotFoundException {
        String collURI = doLookup(lni, collHandle, null);
        URL url = LNIClientUtils.makeDAVURL(endpoint, collURI, packager);
        System.err.println("DEBUG: PUT file=" + source + " to URL=" + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        fixBasicAuth(url, conn);
        conn.connect();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = conn.getOutputStream();
            copyStream(in, out);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("Unable to close input stream", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("Unable to close output stream", e);
                }
            }
        }
        int status = conn.getResponseCode();
        if (status < 200 || status >= 300) {
            die(status, "HTTP error, status=" + String.valueOf(status) + ", message=" + conn.getResponseMessage());
        }
        System.err.println("DEBUG: sent " + source);
        System.err.println("RESULT: Status=" + String.valueOf(conn.getResponseCode()) + " " + conn.getResponseMessage());
        String loc = conn.getHeaderField("Location");
        System.err.println("RESULT: Location=" + ((loc == null) ? "NULL!" : loc));
    }
