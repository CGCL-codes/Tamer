    public static void loginGigaSize() throws IOException {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to Giga Size");
        HttpPost httppost = new HttpPost("http://www.gigasize.com/signin");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("func", ""));
        formparams.add(new BasicNameValuePair("token", formtoken));
        formparams.add(new BasicNameValuePair("signRem", "1"));
        formparams.add(new BasicNameValuePair("email", uname));
        formparams.add(new BasicNameValuePair("password", pwd));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        cookies.setLength(0);
        while (it.hasNext()) {
            escookie = it.next();
            cookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
        }
        System.out.println(cookies);
        if (cookies.toString().contains("MIIS_GIGASIZE_AUTH")) {
            login = true;
        }
        if (login) {
            System.out.println("GigaSize Login Success");
        } else {
            System.out.println("GigaSize Login failed");
        }
    }
