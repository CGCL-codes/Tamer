    public static void loginDropBox() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to dropbox.com");
        HttpPost httppost = new HttpPost("https://www.dropbox.com/login");
        httppost.setHeader("Cookie", gvccookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login_email", "007007dinesh@gmail.com"));
        formparams.add(new BasicNameValuePair("login_password", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("forumjar")) {
                forumjarcookie = "forumjar=" + escookie.getValue();
                System.out.println(forumjarcookie);
            }
            if (escookie.getName().equalsIgnoreCase("touch")) {
                touchcookie = "touch=" + escookie.getValue();
                System.out.println(touchcookie);
            }
            if (escookie.getName().equalsIgnoreCase("forumlid")) {
                forumlidcookie = "forumlid=" + escookie.getValue();
                System.out.println(forumlidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("lid")) {
                lidcookie = "lid=" + escookie.getValue();
                System.out.println(lidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("jar")) {
                jarcookie = "jar=" + escookie.getValue();
                System.out.println(jarcookie);
            }
        }
    }
