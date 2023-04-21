    public boolean doAuthenticate() throws IOException {
        URL url = new URL(loginURL + "?j_username=" + user + "&j_password=" + password);
        logger.debug("HTTP POST request: URL=" + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        try {
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setUseCaches(false);
            conn.connect();
            int rc = conn.getResponseCode();
            logger.debug("HTTP POST response: status=" + rc + ",location=" + conn.getHeaderField("Location"));
            if (rc >= 400) {
                logger.error("Login request failed: rc = " + rc + ": url = " + url);
                return false;
            }
            processSetCookie(conn);
            return true;
        } finally {
            conn.disconnect();
        }
    }
