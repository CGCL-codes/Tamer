    public static String fetchLyric(String artist, String title) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("artist", artist));
        params.add(new BasicNameValuePair("song", title));
        params.add(new BasicNameValuePair("fmt", "json"));
        String paramsList = URLEncodedUtils.format(params, "UTF-8");
        HttpGet request = new HttpGet("http://lyricwiki.org/api.php?" + paramsList);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse execute = client.execute(request);
            String response = EntityUtils.toString(execute.getEntity());
            LOG.d(response);
            if (response != null && response.indexOf('{') > 0) {
                JSONObject obj = new JSONObject(response.substring(response.indexOf('{')));
                return obj.getString("lyrics");
            }
        } catch (ClientProtocolException e) {
            LOG.e("", e);
        } catch (IOException e) {
            LOG.e("", e);
        } catch (JSONException e) {
            LOG.e("", e);
        }
        return "Lyric not found";
    }
