    public static String executeSSLGet(String hostname, String username, String password) throws Exception {
        HttpGet httpget = new HttpGet(hostname);
        HttpResponse response = getSSLClient(username, password).execute(httpget);
        HttpEntity entity = response.getEntity();
        String s = response.getStatusLine().toString();
        if (entity != null) {
            s = Converter.convertInputStreamToString(entity.getContent());
            entity.consumeContent();
        }
        return s;
    }
