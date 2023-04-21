    private static void dispatchRequest(String argURL) {
        try {
            URL url = new URL(argURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.error("JGoogleAnalyticsTracker: Error requesting url '{}', received response code {}", argURL, responseCode);
            } else {
                logger.debug("JGoogleAnalyticsTracker: Tracking success for url '{}'", new Object[] { argURL });
            }
        } catch (Exception e) {
            logger.error("Error making tracking request", e);
        }
    }
