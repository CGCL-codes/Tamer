    private void login(String authToken) throws AuthoricationRequiredException {
        logger.fine("login.");
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("auth", authToken));
            HttpGet httpGet = new HttpGet("/_ah/login?" + URLEncodedUtils.format(nvps, HTTP.UTF_8));
            HttpResponse response = clientManager.httpClient.execute(getHttpsHost(), httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (IOException e) {
            throw new AuthoricationRequiredException(e);
        }
    }
