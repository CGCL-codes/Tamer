    public static String get_feeds_for_current_user(String sessionid, String maxNumberOfFeedString, String owner_alias) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "get_feeds_for_current_user");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Profile"));
            nameValuePairs.add(new BasicNameValuePair("m", "get_feeds"));
            nameValuePairs.add(new BasicNameValuePair("only_public_feeds", "false"));
            nameValuePairs.add(new BasicNameValuePair("count", maxNumberOfFeedString));
            nameValuePairs.add(new BasicNameValuePair("order", "DESC"));
            nameValuePairs.add(new BasicNameValuePair("order_field", "modified"));
            nameValuePairs.add(new BasicNameValuePair("owner_alias", owner_alias));
            nameValuePairs.add(new BasicNameValuePair("before_feed_key", "current_id"));
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
