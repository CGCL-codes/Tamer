    protected String get(String key) throws IOException {
        URL myUrl = new URL(BASE_URL + key);
        log.debug("Opening connection to: " + myUrl);
        HttpURLConnection urlConn = (HttpURLConnection) myUrl.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        if (urlConn.getResponseCode() != 200) {
            log.warn("Recieved error response of " + urlConn.getResponseCode() + " " + urlConn.getRequestMethod());
            return null;
        }
        log.debug("Response successfully recieved");
        DataInputStream input = new DataInputStream(urlConn.getInputStream());
        byte[] data = new byte[VALUE_SIZE];
        int cnt = 0;
        int cur = input.read();
        while (cur != -1) {
            data[cnt] = Byte.valueOf((byte) cur);
            cur = input.read();
            cnt++;
        }
        data = Utilities.shrink(data, cnt);
        log.debug("Recieved response of size: " + data.length);
        return URLDecoder.decode(new String(data, "UTF-8"), "UTF-8");
    }
