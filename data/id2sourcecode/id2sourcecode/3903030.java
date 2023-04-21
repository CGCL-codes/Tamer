    public Map<String, Object> getCurrentUserInfo(String openid, String openkey) {
        if (openid == null || openkey == null) {
            return null;
        }
        try {
            String url = "http://" + openip + "/user/info?openid=" + openid + "&openkey=" + openkey + "&appid=" + appid + "&appkey=" + appkey + "&appname=" + URLEncoder.encode(appname, "UTF-8");
            System.out.println(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse hr = httpclient.execute(httpget);
            String rawString = FileCopyUtils.copyToString(new InputStreamReader(hr.getEntity().getContent(), "UTF-8"));
            JSONObject jso = new JSONObject(rawString);
            System.out.println(jso.toString());
            return JSONUtils.jsonObjectToMap(jso);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
