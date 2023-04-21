    public static void loginUploadedDotTo() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to uploaded.to");
        HttpPost httppost = new HttpPost("http://uploaded.to/io/login");
        httppost.setHeader("Cookie", phpsessioncookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("id", ""));
        formparams.add(new BasicNameValuePair("pw", ""));
        formparams.add(new BasicNameValuePair("_", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("login")) {
                logincookie = "login=" + escookie.getValue();
                System.out.println(logincookie);
                login = true;
            }
            if (escookie.getName().equalsIgnoreCase("auth")) {
                authcookie = "auth=" + escookie.getValue();
                System.out.println(authcookie);
            }
        }
        tmp = getData("http://uploaded.to/");
        userid = parseResponse(tmp, "id=\"user_id\" value=\"", "\"");
        userpwd = parseResponse(tmp, "id=\"user_pw\" value=\"", "\"");
    }
