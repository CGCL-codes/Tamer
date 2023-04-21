    public static String getResult(String fields, String num_iid) {
        String content = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        String timestamp = getFullTime();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("app_key", APP_KEY));
        params.add(new BasicNameValuePair("format", FORMAT));
        params.add(new BasicNameValuePair("method", METHOD));
        params.add(new BasicNameValuePair("num_iid", num_iid));
        params.add(new BasicNameValuePair("fields", fields));
        params.add(new BasicNameValuePair("timestamp", timestamp));
        params.add(new BasicNameValuePair("partner_id", "911"));
        params.add(new BasicNameValuePair("v", VERSION));
        String sign = getSignature(fields, num_iid);
        params.add(new BasicNameValuePair("sign", sign));
        try {
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post Method is not implemented by this URI");
            } else {
                content = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            client.getConnectionManager().shutdown();
        }
        return content + sign;
    }
