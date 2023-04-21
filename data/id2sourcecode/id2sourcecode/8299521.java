    public JSONObject processWebRequest(JSONObject jobj, String prefix) throws UnknownHostException, IOException, HttpException, InterruptedIOException {
        JSONObject ret = null;
        prefixTrace = prefix;
        String url = localSettiingsFactory.refreshUrlCache();
        HttpPost httprequest = new HttpPost(url);
        if (x_transmission_session_id != null && authenticate) {
            httprequest.setHeader("X-Transmission-Session-Id", x_transmission_session_id);
            httprequest.setEntity(new StringEntity(jobj.toString()));
            tracePrint("Request=" + jobj.toString());
            starWebRequest = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(targetHttpHost, httprequest);
            stopWebRequest = System.currentTimeMillis();
            diffWebRequest = stopWebRequest - starWebRequest;
            tracePrint("[getStatusLine()]----------------------------------------");
            tracePrint(response.getStatusLine().toString());
            tracePrint("[getStatusLine()]----------------------------------------");
            HttpEntity entity = response.getEntity();
            tracePrint("[processWebRequest][consumeContent()]----------------------------------------");
            responseData = null;
            if (entity != null) {
                tracePrint(true, "[processWebRequest] Response content length: [" + entity.getContentLength() + "] time: [" + diffWebRequest + "]");
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT) {
                    String old_x_transmission_session_id = x_transmission_session_id;
                    x_transmission_session_id = getTansmissionSessionId(response);
                    tracePrint(true, "[processWebRequest] Change x_transmission_session_id [" + old_x_transmission_session_id + "]->[" + x_transmission_session_id + "]]");
                }
                responseData = EntityUtils.toString(entity);
                tracePrint(responseData);
                try {
                    ret = JSONObject.fromObject(responseData);
                } catch (net.sf.json.JSONException ex) {
                    ret = null;
                }
            }
            if (entity != null) {
                entity.consumeContent();
            }
            tracePrint("[processWebRequest][consumeContent()]----------------------------------------");
        }
        return ret;
    }
