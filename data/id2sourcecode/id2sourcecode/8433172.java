        @Override
        public void run() {
            try {
                HttpURLConnection ucon = (HttpURLConnection) _url.openConnection();
                if (_app.getTrackingCookieName() != null && _app.getTrackingCookieProperty() != null) {
                    String val = System.getProperty(_app.getTrackingCookieProperty());
                    if (val != null) {
                        ucon.setRequestProperty("Cookie", _app.getTrackingCookieName() + "=" + val);
                    }
                }
                ucon.connect();
                try {
                    if (ucon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        log.warning("Failed to report tracking event", "url", _url, "rcode", ucon.getResponseCode());
                    }
                } finally {
                    ucon.disconnect();
                }
            } catch (IOException ioe) {
                log.warning("Failed to report tracking event", "url", _url, "error", ioe);
            }
        }
