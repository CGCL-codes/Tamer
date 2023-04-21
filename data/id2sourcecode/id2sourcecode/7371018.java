    private void openInputStream() {
        try {
            URL url = new URL(urlString);
            try {
                HttpURLConnection.setFollowRedirects(true);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686 (x86_64); en-US; rv:1.7.10) Gecko/20061113 Firefox/1.0.4 (Debian package 1.0.4-2sarge13)");
                connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
                connection.setRequestProperty("Accept-Language", "en_us");
                connection.setConnectTimeout(timeout);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    logger.warn("HttpServer returned an error code (" + urlString + "): " + connection.getResponseCode());
                    connection = null;
                }
                if (connection != null) {
                    is = connection.getInputStream();
                }
            } catch (SocketTimeoutException e) {
                logger.warn("Timeout (" + urlString + "): " + " " + e.toString());
            }
        } catch (Exception e) {
            logger.warn("Error connecting to http-Server (" + urlString + "): ", e);
        }
        return;
    }
