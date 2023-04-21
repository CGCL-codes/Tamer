    public void asynchronousWrite(String key, Object obj) throws Exception {
        String urlStr = _httpRoot + REMOTE_STORE + "/" + makeFilename(key);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        ObjectOutputStream oos = getObjectOutputStream(conn.getOutputStream());
        oos.writeObject(obj);
        oos.close();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Problem putting response to server! " + urlStr + " ==> " + conn.getResponseCode());
        }
    }
