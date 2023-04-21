    public List<Mosque> getAllMosquaisFromGoogleAPI() {
        List<Mosque> mosquais = new ArrayList<Mosque>();
        String result = "";
        String Url = Param.URL_GOOGLE_MAP_QUERY;
        String mLatitude = "";
        String mLongitude = "";
        if (MyMapActivity.DEVELOPER_MODE) {
            mLatitude = MyMapActivity.mLatitude + "";
            mLongitude = MyMapActivity.mLongitude + "";
        } else {
            mLatitude = MyMapActivity.myLocation.getLatitude() + "";
            mLongitude = MyMapActivity.myLocation.getLongitude() + "";
        }
        Url += "q=mosque";
        Url += "&sll=" + mLatitude + "," + mLongitude;
        Url += "&sensor=true&output=json";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        mosquais = DataUtils.ExtracteMosquesFromHttpRequest(result);
        mosquais = FiltreByRadius(mosquais, Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
        return mosquais;
    }
