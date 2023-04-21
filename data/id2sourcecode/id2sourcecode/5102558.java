    @Override
    public boolean isLiving() {
        boolean result = true;
        log.debug("Starting to ping to {}", url);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                log.debug("return HTTP response code: {}", responseCode);
                result = false;
            }
        } catch (IOException ex) {
            log.info("ping {} failed.", url, ex);
            result = false;
        }
        return result;
    }
