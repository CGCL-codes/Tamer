    public static void loginBox() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to box.com");
        HttpPost httppost = new HttpPost("https://www.box.net/api/1.0/auth/" + ticket);
        httppost.setHeader("Cookie", zcookie + ";" + visitorcookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login", "din_ram2007@yahoo.co.in"));
        formparams.add(new BasicNameValuePair("password", ""));
        formparams.add(new BasicNameValuePair("__login", "1"));
        formparams.add(new BasicNameValuePair("dologin", "1"));
        formparams.add(new BasicNameValuePair("reg_step", ""));
        formparams.add(new BasicNameValuePair("submit1", "1"));
        formparams.add(new BasicNameValuePair("folder", ""));
        formparams.add(new BasicNameValuePair("skip_framework_login", "1"));
        formparams.add(new BasicNameValuePair("login_or_register_mode", "login"));
        formparams.add(new BasicNameValuePair("new_login_or_register_mode", ""));
        formparams.add(new BasicNameValuePair("request_token", request_token));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Gonna print the response");
        loginresponse = EntityUtils.toString(httpresponse.getEntity());
        if (loginresponse.contains("Invalid username or password")) {
            System.out.println("DropBox login failed");
        } else {
            System.out.println("DropbBox login successful :)");
        }
    }
