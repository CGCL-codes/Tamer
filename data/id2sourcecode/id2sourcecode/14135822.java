    public static double[] getLocationInfo(String address) {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://maps.google." + "com/maps/api/geocode/json?address=" + address + "ka&sensor=false");
        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                sb.append((char) b);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            double longitude = location.getDouble("lng");
            double latitude = location.getDouble("lat");
            return new double[] { longitude, latitude };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
