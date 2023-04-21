    public static void loginUGotFile() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to ugotfile.com");
        HttpPost httppost = new HttpPost("http://ugotfile.com/user/login");
        httppost.setHeader("Cookie", phpsessioncookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("ugfLoginUserName", ""));
        formparams.add(new BasicNameValuePair("ugfLoginPassword", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity resEntity = httpresponse.getEntity();
        if (httpresponse.getStatusLine().toString().contains("302")) {
            tmp = httpresponse.getLastHeader("Location").getValue();
            System.out.println("UGotFile Login success");
        } else {
            System.out.println("UGotFile login failed");
        }
    }
