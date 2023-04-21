    private void doDrawActionHistory() {
        String serverUrl = AppPreferences.getServerUrl(this.getApplicationContext());
        String loginId = AppPreferences.getLoginId(this.getApplicationContext());
        Date fromDate = actionHistoryFrom.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d-kk:mm");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(timeZone);
        String dateBeginStr = sdf.format(fromDate);
        Calendar now = Calendar.getInstance(timeZone);
        Date toDate = now.getTime();
        String dateEnd = sdf.format(toDate);
        try {
            String locationFeed = "http://" + serverUrl + "/api?loginid=" + loginId + "&datebegin=" + dateBeginStr + "&dateend=" + dateEnd;
            Log.i(TAG, locationFeed);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
            HttpConnectionParams.setSoTimeout(params, 2 * 1000);
            HttpClient objHttp = new DefaultHttpClient(params);
            HttpGet objGet = new HttpGet(locationFeed);
            HttpResponse objResponse = objHttp.execute(objGet);
            if (objResponse.getStatusLine().getStatusCode() == 200) {
                InputStream objStream = objResponse.getEntity().getContent();
                String jsonString = convertStreamToString(objStream);
                Type type = new TypeToken<List<GPSRecord>>() {
                }.getType();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Gson gson = gsonBuilder.create();
                List<GPSRecord> locationList = gson.fromJson(jsonString, type);
                if (locationList.size() > 1) {
                    positionOverlay.setActionHistory(locationList);
                    positionOverlay.setDisplayActionHistory(true);
                    final GPSRecord locr = locationList.get(0);
                    mHandler.post(new Runnable() {

                        public void run() {
                            Double geoLat = locr.getLatitude() * 1E6;
                            Double geoLng = locr.getLongitude() * 1E6;
                            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
                            mapController.animateTo(point);
                            mMapView.invalidate();
                            Toast.makeText(mApplicationContext, R.string.history_found, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {

                        public void run() {
                            Toast.makeText(mApplicationContext, R.string.history_not_found, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        } catch (SocketTimeoutException e) {
            mHandler.post(new Runnable() {

                public void run() {
                    Toast.makeText(mApplicationContext, R.string.cannot_connect_server, Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            mHandler.post(new Runnable() {

                public void run() {
                    Toast.makeText(mApplicationContext, R.string.io_error, Toast.LENGTH_LONG).show();
                }
            });
        } finally {
        }
    }
