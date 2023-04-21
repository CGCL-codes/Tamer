    private InputStream getInputStream(URL url) {
        try {
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            final int responseCode = conn.getResponseCode();
            mLogger.debug(Integer.toString(responseCode) + " - " + conn.getResponseMessage());
            if (responseCode == 200) return conn.getInputStream(); else return null;
        } catch (IOException ie) {
            return null;
        }
    }
