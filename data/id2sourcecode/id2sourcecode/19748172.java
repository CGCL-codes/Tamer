    public static void loginSendSpace() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to sendspace");
        HttpPost httppost = new HttpPost("http://www.sendspace.com/login.html");
        httppost.setHeader("Cookie", sidcookie + ";" + ssuicookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("action", "login"));
        formparams.add(new BasicNameValuePair("submit", "login"));
        formparams.add(new BasicNameValuePair("target", "%252F"));
        formparams.add(new BasicNameValuePair("action_type", "login"));
        formparams.add(new BasicNameValuePair("remember", "1"));
        formparams.add(new BasicNameValuePair("username", ""));
        formparams.add(new BasicNameValuePair("password", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("ssal")) {
                ssalcookie = escookie.getName() + "=" + escookie.getValue();
                System.out.println(ssalcookie);
                login = true;
            }
        }
    }
