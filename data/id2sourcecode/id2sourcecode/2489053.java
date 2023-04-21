    private byte[] getCredentials() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(CredUrl);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("user_session[email]", this.email));
        formparams.add(new BasicNameValuePair("user_session[password]", this.password));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        } catch (Exception e) {
        }
        post.setEntity(entity);
        byte[] xml = null;
        try {
            HttpResponse res = httpClient.execute(post);
            int status = res.getStatusLine().getStatusCode();
            if (status == 200) {
                xml = EntityUtils.toByteArray(res.getEntity());
            }
        } finally {
            post.abort();
        }
        return xml;
    }
