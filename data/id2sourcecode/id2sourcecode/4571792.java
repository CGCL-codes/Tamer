    public void get(GuseServiceBean t) {
        try {
            if (!"".equals(t.getIurl()) && !"null".equals(t.getIurl())) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("is.url", PropertyLoader.getInstance().getProperty("is.url")));
                nvps.add(new BasicNameValuePair("is.id", PropertyLoader.getInstance().getProperty("is.id")));
                nvps.add(new BasicNameValuePair("service.url", t.getUrl()));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
