    private static InputStream readFrom(String urlStr, String postStr) throws MalformedURLException, IOException {
        URLConnection conn = new URL(urlStr).openConnection();
        conn.setDoInput(true);
        if (postStr != null && postStr.length() > 0) {
            conn.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            output.writeBytes(postStr);
            output.flush();
            output.close();
        }
        return conn.getInputStream();
    }
