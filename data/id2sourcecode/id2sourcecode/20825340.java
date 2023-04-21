    @Override
    public void download(URL url) throws Exception {
        HttpURLConnection conn = openConnection(url);
        conn.setInstanceFollowRedirects(this.followRedirects);
        conn.connect();
        if (conn.getResponseCode() == 200) {
            ddHandler.processData(new BinaryData(readData(conn), conn.getHeaderFields(), url));
        } else {
            throw new IOException("Error fetching " + url.toString());
        }
    }
