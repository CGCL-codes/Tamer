    public static void loginZohoDocs() throws IOException {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to Zoho Docs");
        HttpPost httppost = new HttpPost("https://accounts.zoho.com/login");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("LOGIN_ID", uname));
        formparams.add(new BasicNameValuePair("PASSWORD", pwd));
        formparams.add(new BasicNameValuePair("IS_AJAX", "true"));
        formparams.add(new BasicNameValuePair("remember", "-1"));
        formparams.add(new BasicNameValuePair("servicename", "ZohoPC"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            cookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
            System.out.println(cookies);
        }
        if (cookies.toString().contains(uname)) {
            login = true;
        }
        if (login) {
            System.out.println("Zoho Docs Login Success");
        } else {
            System.out.println("Zoho Docs Login failed");
        }
    }
