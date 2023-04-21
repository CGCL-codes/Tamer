    protected static InputStream getHttpGETInputStream(String urlStr) {
        if (urlStr == null) throw new NullPointerException();
        InputStream is = null;
        URLConnection conn = null;
        try {
            if (urlStr.startsWith("file://")) return new FileInputStream(urlStr.replace("file://", ""));
            URL url = new URL(urlStr);
            conn = url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            is = conn.getInputStream();
            return is;
        } catch (Exception ex) {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                if (conn instanceof HttpURLConnection) ((HttpURLConnection) conn).disconnect();
            } catch (Exception e) {
            }
            ex.printStackTrace();
        }
        return null;
    }
