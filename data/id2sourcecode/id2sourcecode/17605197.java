    public InputStream SendRequest(String query) throws Exception {
        if (query == null || query.length() == 0) throw new IllegalArgumentException("query is required");
        final URL url = getURL(mUsername, mHashword, query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        conn.addRequestProperty("Accept-encoding", "gzip");
        int responseCode = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        if (conn.getContentEncoding().equalsIgnoreCase("gzip")) {
            in = new java.util.zip.GZIPInputStream(in);
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return in;
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            throw new Exception(sb.toString());
        }
    }
