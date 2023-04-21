    public static void loginUploadBox() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to uploadbox.com");
        HttpPost httppost = new HttpPost("http://www.uploadbox.com/en");
        httppost.setHeader("Cookie", sidcookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login", ""));
        formparams.add(new BasicNameValuePair("passwd", ""));
        formparams.add(new BasicNameValuePair("ac", "auth"));
        formparams.add(new BasicNameValuePair("back", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity resEntity = httpresponse.getEntity();
        System.out.println(httpresponse.getStatusLine());
    }
