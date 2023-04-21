    public static void loginSlingFile() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to slingfile.com");
        HttpPost httppost = new HttpPost("http://www.slingfile.com/login");
        httppost.setHeader("Cookie", cookie.toString() + ";signupreferrerurl=http%3A%2F%2Fwww.slingfile.com%2F;");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("f_user", "dineshs"));
        formparams.add(new BasicNameValuePair("f_password", "*********************"));
        formparams.add(new BasicNameValuePair("submit", "Login »"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println(httpresponse.getStatusLine());
        if (httpresponse.getFirstHeader("Location").getValue().contains("dashboard")) {
            login = true;
            cookie.append(";signupreferrerurl=http%3A%2F%2Fwww.slingfile.com%2F;");
        } else {
            login = false;
        }
        if (login) {
            System.out.println("SlingFile Login successful :)");
        } else {
            System.out.println("SlingFile Login failed :(");
        }
    }
