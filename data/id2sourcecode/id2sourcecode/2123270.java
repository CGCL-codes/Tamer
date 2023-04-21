    protected boolean check() {
        if (config == null) return true;
        HttpClient client = new DefaultHttpClient();
        boolean result = false;
        try {
            HttpResponse response = client.execute(new HttpGet(config.getUrl()));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (Exception e) {
            if (isNormal) LOG.error(e.getMessage());
        }
        return result;
    }
