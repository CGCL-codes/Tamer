    public static String getAddress(double longitude, double latitude) {
        HttpParams httpParams = new DefaultHttpClient().getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 3000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/" + "geocode/json?latlng=" + latitude + "," + longitude + "&language=zh&sensor=false&region=cn");
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            JSONObject jsonObj = new JSONObject(EntityUtils.toString(entity));
            return jsonObj.getJSONArray("results").getJSONObject(0).getString("formatted_address");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
