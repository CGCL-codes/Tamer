    private void sendLog() {
        HttpClient hc = new DefaultHttpClient();
        String url = "http://gae-for-android.appspot.com/wifi/upload";
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("time", mStringDayTime));
        params.add(new BasicNameValuePair("lineType", mStringLineType));
        params.add(new BasicNameValuePair("throughput", mStringThroughput));
        params.add(new BasicNameValuePair("strength", mStringElectricFieldStrength));
        params.add(new BasicNameValuePair("latitude", mStringLatitude));
        params.add(new BasicNameValuePair("longitude", mStringLongitude));
        params.add(new BasicNameValuePair("bssId", mStringBssid));
        params.add(new BasicNameValuePair("mcAddr", mStringMacAddress));
        params.add(new BasicNameValuePair("model", mStringModel));
        params.add(new BasicNameValuePair("ssId", mStringSsid));
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse res = hc.execute(post);
            int status = res.getStatusLine().getStatusCode();
            if (!(status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED)) {
                throw new RuntimeException("Request timeout");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
