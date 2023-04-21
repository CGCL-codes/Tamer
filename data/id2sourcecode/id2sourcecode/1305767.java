    private void requestcoord() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new name_value("uid", result));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.gotrackit.net/server/getbuscoord.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection" + e.toString());
        }
        try {
            int r = -1;
            StringBuffer reader = new StringBuffer();
            while ((r = is.read()) != -1) reader.append((char) r);
            String tagOpenlat = "<`latitude`>";
            String tagCloselat = "<`/latitude`>";
            String tagOpenlng = "<`longitude`>";
            String tagCloselng = "<`/longitude`>";
            if (reader.indexOf(tagOpenlat) != -1) {
                int start = reader.indexOf(tagOpenlat) + tagOpenlat.length();
                int end = reader.indexOf(tagCloselat);
                String value = reader.substring(start, end);
                blatitude = Double.parseDouble(value);
            }
            if (reader.indexOf(tagOpenlng) != -1) {
                int start = reader.indexOf(tagOpenlng) + tagOpenlng.length();
                int end = reader.indexOf(tagCloselng);
                String value = reader.substring(start, end);
                blongitude = Double.parseDouble(value);
            }
            is.close();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        String bus = "latitude = " + blatitude + "\nlongitude = " + blongitude;
        Toast toast = Toast.makeText(context, bus, duration);
        toast.show();
        mController = mapView.getController();
        geoPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        overlayitem = new OverlayItem(geoPoint, "Me", "");
        itemizedOverlay2.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay2);
        GeoPoint point = new GeoPoint((int) (blatitude * 1E6), (int) (blongitude * 1E6));
        overlayitem = new OverlayItem(point, "Bus", "");
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        mController.animateTo(point);
        mController.setCenter(point);
        mController.setZoom(17);
    }
