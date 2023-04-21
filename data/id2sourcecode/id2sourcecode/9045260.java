    private void sendData(Bundle values) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://sleepsocial.appspot.com/connections/facebook/connect.htm");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(Facebook.TOKEN, values.getString(Facebook.TOKEN)));
            nameValuePairs.add(new BasicNameValuePair("expires", values.getString(Facebook.EXPIRES)));
            nameValuePairs.add(new BasicNameValuePair("secret", "secret"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            byte[] data = new byte[1024];
            response.getEntity().getContent().read(data);
            ((TextView) findViewById(R.id.txt)).setText(new String(data));
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
    }
