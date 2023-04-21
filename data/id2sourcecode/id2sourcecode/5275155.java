    public static void loginNetload() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to netload.in");
        HttpPost httppost = new HttpPost("http://netload.in/index.php");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("txtuser", "663167"));
        formparams.add(new BasicNameValuePair("txtpass", ""));
        formparams.add(new BasicNameValuePair("txtcheck", "login"));
        formparams.add(new BasicNameValuePair("txtlogin", "Login"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("cookie_user")) {
                usercookie = "cookie_user=" + escookie.getValue();
                System.out.println(usercookie);
                login = true;
                System.out.println("Netload Login success :)");
                initialize();
            }
        }
        if (!login) {
            System.out.println("Netload Login failed :(");
        }
    }
