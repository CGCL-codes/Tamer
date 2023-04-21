    public static String postUrlData(HttpClient httpclient, String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
        String reponse = null;
        if (Common.DEBUG) Log.d(Common.TAG, "POST : " + url);
        HttpPost post = new HttpPost(url);
        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = httpclient.execute(post);
            if (Common.DEBUG) Log.i(Common.TAG, "reponse " + responsePOST.getStatusLine());
            HttpEntity entity = responsePOST.getEntity();
            if (Common.DEBUG) Log.d(Common.TAG, "Response size=" + entity.getContentLength());
            if (responsePOST.getStatusLine().getStatusCode() == 200) {
                if (entity != null) {
                    reponse = Common.convertStreamToString(entity);
                } else {
                    reponse = responsePOST.getStatusLine().toString();
                }
            } else {
                ClientProtocolException e = new ClientProtocolException(responsePOST.getStatusLine().toString());
                throw e;
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        if (Common.DEBUG) Log.d(Common.TAG, "POST reponse: " + reponse);
        return reponse;
    }
