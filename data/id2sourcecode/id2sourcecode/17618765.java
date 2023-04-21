    private boolean isReachable() {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) _url.openConnection();
            httpConnection.setRequestMethod("HEAD");
            httpConnection.setConnectTimeout(MAX_WAIT);
            httpConnection.getResponseCode();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }
