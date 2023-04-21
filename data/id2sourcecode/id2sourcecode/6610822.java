    public static void loginFileDen() throws Exception {
        cookies = new StringBuilder();
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to fileden.com");
        HttpPost httppost = new HttpPost("http://www.fileden.com/account.php?action=login");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("action", "login"));
        formparams.add(new BasicNameValuePair("task", "login"));
        formparams.add(new BasicNameValuePair("username", "007007dinesh"));
        formparams.add(new BasicNameValuePair("password", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            cookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
        }
        if (cookies.toString().contains("uploader_username")) {
            login = true;
        }
        if (login) {
            System.out.println("FileDen Login success :)");
            System.out.println(cookies);
        } else {
            System.out.println("FileDen Login failed :(");
        }
    }
