    private void updateLocation2(Location location) {
        Log.i(TAG, "updateLocation2");
        if (location == null) return;
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        updateLocationCallbackFunc(lat, lon);
        Log.d(TAG, "location(" + lat + "," + lon + ")");
        String serverUrl = AppPreferences.getServerUrl(this.getApplicationContext());
        if (serverUrl.length() == 0) return;
        String loginId = AppPreferences.getLoginId(this.getApplicationContext());
        String postUrl = "http://" + serverUrl + "/api";
        Log.i(TAG, postUrl);
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 3000);
        HttpClient httpclient = new DefaultHttpClient(params);
        HttpPost httppost = new HttpPost(postUrl);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("loginid", loginId));
            nameValuePairs.add(new BasicNameValuePair("latitude", Double.toString(lat)));
            nameValuePairs.add(new BasicNameValuePair("longitude", Double.toString(lon)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            response.getEntity();
        } catch (SocketTimeoutException e) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, R.string.cannot_connect_server, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } catch (IOException e) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, R.string.io_error, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } finally {
        }
    }
