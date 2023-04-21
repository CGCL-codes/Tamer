    public boolean validateBaseURL(URL baseURL) {
        if (baseURL == null) {
            return false;
        }
        boolean valid = false;
        try {
            HttpGet get = new HttpGet(baseURL.toString() + PATH_TO_CSS);
            HttpResponse response = http.executeGetWithTimeout(get, 4000);
            valid = response.getStatusLine().getStatusCode() == 200;
        } catch (ClientProtocolException e) {
            log.info("Protocol error connecting to new base URL [" + baseURL.toString() + "]", e);
        } catch (IOException e) {
            log.info("IO error connecting to new base URL [" + baseURL.toString() + "]", e);
        } catch (Exception e) {
            log.info("Unknown error connecting to new base URL [" + baseURL.toString() + "]", e);
        }
        return valid;
    }
