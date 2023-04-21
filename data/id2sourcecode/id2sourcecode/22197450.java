    public static void loginCrocko() throws Exception {
        cookies = new StringBuilder();
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to crocko.com");
        HttpPost httppost = new HttpPost("https://www.crocko.com/accounts/login");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login", "007007dinesh"));
        formparams.add(new BasicNameValuePair("password", "*********************"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        System.out.println(EntityUtils.toString(httpresponse.getEntity()));
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            cookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
            if (escookie.getName().equals("PHPSESSID")) {
                sessionid = escookie.getValue();
                System.out.println(sessionid);
            }
        }
        if (cookies.toString().contains("logacc")) {
            System.out.println(cookies);
            login = true;
            System.out.println("Crocko login successful :)");
            getData();
        }
        if (!login) {
            System.out.println("Crocko.com Login failed :(");
        }
    }
