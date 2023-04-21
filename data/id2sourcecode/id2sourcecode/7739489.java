    public static String doPost(String url, List<NameValuePair> nameValuePairs) throws Exception {
        StringBuffer sb = new StringBuffer();
        HttpPost httppost = new HttpPost(url);
        BasicHttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            String line = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
