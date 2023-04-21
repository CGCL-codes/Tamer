    public String Get(String url) {
        HttpGet get = new HttpGet(url);
        HttpEntity entity = null;
        String stringResponse = null;
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                entity = response.getEntity();
                stringResponse = EntityUtils.toString(entity);
            }
        } finally {
            return stringResponse;
        }
    }
