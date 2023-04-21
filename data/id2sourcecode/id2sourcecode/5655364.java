    public static void loginOneFichier() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to 1fichier.com");
        HttpPost httppost = new HttpPost("http://www.1fichier.com/en/login.pl");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("mail", "007007dinesh@gmail.com"));
        formparams.add(new BasicNameValuePair("pass", ""));
        formparams.add(new BasicNameValuePair("Login", "Login"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("SID")) {
                sidcookie = "SID=" + escookie.getValue();
                System.out.println(sidcookie);
                login = true;
            }
        }
        if (login) {
            System.out.println("1fichier Login successful :)");
        } else {
            System.out.println("1fichier Login failed :(");
        }
    }
