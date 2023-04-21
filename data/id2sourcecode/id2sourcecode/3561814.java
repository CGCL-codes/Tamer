    public static JSONArray Post(String pathSegment, JSONObject params) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
        URI url = URI.create(Variables.BASE_URI + pathSegment);
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpPost);
        return parseResponse(response);
    }
