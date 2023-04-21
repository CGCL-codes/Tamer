    public static String downloadString(URL url, Logger logger) {
        String ret = null;
        URLConnection con = null;
        try {
            con = url.openConnection();
            Class<? extends URLConnection> cls = con.getClass();
            Object[] timeout = new Object[] { new Integer(6000) };
            Method mth = cls.getMethod("setConnectTimeout", new Class[] { int.class });
            if (mth != null) {
                mth.invoke(con, timeout);
            }
            mth = cls.getMethod("setReadTimeout", new Class[] { int.class });
            if (mth != null) {
                mth.invoke(con, timeout);
            }
            con.connect();
            InputStream in = con.getInputStream();
            if (con instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) con;
                int code = http.getResponseCode();
                logger.debug("\tDownload response code = " + code);
                if (code == 200) {
                    int len = con.getContentLength();
                    logger.debug("Downloading content len= " + len);
                    if (len < 0) {
                        len = 1024 * 1024 * 5;
                    }
                    byte[] data = new byte[len];
                    int got = 0;
                    while (got < len) {
                        int gotNow = in.read(data, got, (len - got));
                        if (gotNow < 0) {
                            break;
                        } else {
                            got += gotNow;
                        }
                    }
                    ret = new String(data, 0, got);
                } else if (code == 304) {
                } else {
                    logger.debug("Response code " + code + " indicated a problem. Use local if possible.");
                }
            } else {
            }
        } catch (Throwable e) {
        }
        return ret;
    }
