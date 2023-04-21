    void insertINTO(String etykieta1, String login, String etykieta2, String password) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(etykieta1, login));
        nameValuePairs.add(new BasicNameValuePair(etykieta2, password));
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://**.*.*.*/OctopusManager/test.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
