    private boolean resourceExists(URL url) {
        boolean ok = true;
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof java.net.HttpURLConnection) {
                java.net.HttpURLConnection hconn = (java.net.HttpURLConnection) conn;
                int code = hconn.getResponseCode();
                if (code == java.net.HttpURLConnection.HTTP_OK) {
                    return true;
                }
                if (code >= java.net.HttpURLConnection.HTTP_BAD_REQUEST) {
                    return false;
                }
            } else {
                InputStream is = url.openStream();
                is.close();
            }
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }
