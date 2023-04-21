    public void TestJSON() {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            JSONObject object;
            URI uri = new URI(urlStr);
            HttpPost post = new HttpPost(uri);
            object = new JSONObject();
            object.put("login", "Makr");
            object.put("password", "test");
            logView.append("\n" + object.toString());
            HttpEntity entity = new StringEntity(object.toString());
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                InputStream instream = httpEntity.getContent();
                String result = convertStreamToString(instream);
                logView.append("\n" + result);
                try {
                    JSONObject json = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                instream.close();
            }
        } catch (Exception e) {
            logView.append("\n" + e.getMessage());
        }
    }
