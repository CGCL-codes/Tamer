    public void login(RequestListener r, SharedPreferences prefs) throws ClientProtocolException, IOException {
        mListener = r;
        mUserSettings = prefs;
        if (mHttpClient == null) mHttpClient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(MFCService.SIGN_IN_URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", mUserSettings.getString(Credentials.LOGIN, "")));
        nvps.add(new BasicNameValuePair("password", mUserSettings.getString(Credentials.PASSWORD, "")));
        nvps.add(new BasicNameValuePair("set_cookie", "1"));
        nvps.add(new BasicNameValuePair("commit", "signin"));
        nvps.add(new BasicNameValuePair("location", "http%3A%2F%2Fmyfigurecollection.net%2F"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        HttpResponse response = mHttpClient.execute(httpost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity.consumeContent();
        }
        List<Cookie> cookies = mHttpClient.getCookieStore().getCookies();
        String sessionkey = null;
        Cookie sessionCookie = null;
        if (cookies.isEmpty()) {
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                Cookie myCookie = cookies.get(i);
                String cookieString = myCookie.getName() + "=" + myCookie.getValue() + "; domain=" + myCookie.getDomain();
                try {
                    Credentials.cookieManager.setCookie(Credentials.DOMAIN, cookieString);
                } catch (NullPointerException e) {
                }
                if (cookies.get(i).getName().compareTo("tb_session_key") == 0) sessionCookie = (cookies.get(i));
            }
        }
        CookieSyncManager.getInstance().sync();
        mListener.onRequestcompleted(response.getStatusLine().getStatusCode(), sessionCookie);
    }
