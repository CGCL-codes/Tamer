    public boolean send(Measure measure) throws ClientProtocolException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        logger.debug("response line: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
            logger.debug("contenido: " + entity.getContent().toString());
        }
        logger.debug("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            logger.debug("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                logger.debug("- " + cookies.get(i).toString());
            }
        }
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("data", measure.getData().toString()));
        nvps.add(new BasicNameValuePair("type", measure.getType().name().substring(0, 1)));
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        nvps.add(new BasicNameValuePair("when", dateFormat.format(measure.getDateIs())));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        response = httpclient.execute(httpost);
        entity = response.getEntity();
        logger.debug("statys line: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        logger.debug("request cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            logger.debug("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                logger.debug("- " + cookies.get(i).toString());
            }
        }
        httpclient.getConnectionManager().shutdown();
        return true;
    }
