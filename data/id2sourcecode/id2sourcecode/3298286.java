    public static String create_new_tag(String sessionid, String parentTagName, String tagName) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "create_new_tag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "create_new_tag"));
            nameValuePairs.add(new BasicNameValuePair("parent_tag", parentTagName));
            nameValuePairs.add(new BasicNameValuePair("tag", tagName));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }
