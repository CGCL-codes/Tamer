    private void addChild(String id) {
        HttpPost httpPost = new HttpPost(dbManager.getConnectionUrl() + "Cimande2/parent/" + bundle.getString("id") + "/addC");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("cid", id));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            Log.i("Android JSON", response.getStatusLine().toString());
            Log.i("Sending data to", httpPost.getURI().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = new ConnectionManager().convertStreamToString(instream);
                Log.i("Response", result);
                instream.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
