    private static void flush() {
        int respCode = -1;
        HttpPost request = new HttpPost(respURI);
        JSONObject param = new JSONObject();
        try {
            param.put("ID", id);
            param.put("DEV", dev);
            param.put("TAG", tag);
            param.put("MSG", msg);
            param.put("TIME", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        synchronized (lockey) {
            reporting = false;
        }
        Log.d("LamrimReader", "gson=" + param.toString());
        StringEntity se;
        try {
            se = new StringEntity(param.toString());
            request.setEntity(se);
            HttpResponse httpResp = new DefaultHttpClient().execute(request);
            respCode = httpResp.getStatusLine().getStatusCode();
            msg = new JSONArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("LogRepoter", "Write msg to remote host return: " + respCode);
    }
