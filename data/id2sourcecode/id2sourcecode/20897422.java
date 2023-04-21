    public String SendAndWaitResponse(String strReqData, String strUrl) {
        detectProxy();
        String strResponse = null;
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("requestData", strReqData));
        HttpURLConnection httpConnect = null;
        UrlEncodedFormEntity p_entity;
        try {
            p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
            URL url = new URL(strUrl);
            if (mProxy != null) {
                httpConnect = (HttpURLConnection) url.openConnection(mProxy);
            } else {
                httpConnect = (HttpURLConnection) url.openConnection();
            }
            httpConnect.setConnectTimeout(connectTimeout);
            httpConnect.setReadTimeout(readTimeout);
            httpConnect.setDoOutput(true);
            httpConnect.addRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            httpConnect.connect();
            OutputStream os = httpConnect.getOutputStream();
            p_entity.writeTo(os);
            os.flush();
            InputStream content = httpConnect.getInputStream();
            strResponse = BaseHelper.convertStreamToString(content);
            BaseHelper.log(TAG, "response " + strResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpConnect.disconnect();
        }
        return strResponse;
    }
