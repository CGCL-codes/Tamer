    public JSONObject lastTweet(String username) throws ClientProtocolException, IOException, JSONException {
        StringBuilder sbURL = new StringBuilder(url);
        sbURL.append(username);
        HttpGet get = new HttpGet(sbURL.toString());
        HttpResponse r = client.execute(get);
        int status = r.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity e = r.getEntity();
            String data = EntityUtils.toString(e);
            JSONArray timeline = new JSONArray(data);
            JSONObject last = timeline.getJSONObject(0);
            return last;
        } else {
            Toast.makeText(HttpExample.this, "error", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
