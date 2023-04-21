    public static void loginZiddu() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to ziddu");
        HttpPost httppost = new HttpPost("http://www.ziddu.com/login.php");
        httppost.setHeader("Referer", "http://www.ziddu.com/");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email", "007007dinesh@gmail.com"));
        formparams.add(new BasicNameValuePair("password", ""));
        formparams.add(new BasicNameValuePair("action", "LOGIN"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        System.out.println(httpresponse.getStatusLine());
        Header[] allHeaders = httpresponse.getAllHeaders();
        for (int i = 0; i < allHeaders.length; i++) {
            System.out.println(allHeaders[i].getName() + " = " + allHeaders[i].getValue());
        }
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().contains("PHPSESSID")) {
                phpsessioncookie = escookie.getName() + " = " + escookie.getValue();
            }
            InputStream is = httpresponse.getEntity().getContent();
            is.close();
            System.out.println("php session cookie : " + phpsessioncookie);
        }
    }
