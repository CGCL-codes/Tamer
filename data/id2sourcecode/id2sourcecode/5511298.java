    public static void loginDivShare() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to divshare.com");
        HttpPost httppost = new HttpPost("http://www.divshare.com/login");
        httppost.setHeader("Cookie", cookie.toString());
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login_submit", "Login >"));
        formparams.add(new BasicNameValuePair("user_email", "007007dinesh@gmail.com"));
        formparams.add(new BasicNameValuePair("user_password", "******************"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println(httpresponse.getStatusLine());
        Header[] allHeaders = httpresponse.getAllHeaders();
        for (int i = 0; i < allHeaders.length; i++) {
            if (allHeaders[i].getName().contains("Location")) {
                if (allHeaders[i].getValue().contains("members")) {
                    login = true;
                }
                String tmp = cookie.toString();
                tmp = tmp.replace("cntHeaderMessages=0; path=/; domain=.divshare.com;", "");
                cookie.setLength(0);
                cookie.append(tmp);
                System.out.println(cookie);
                break;
            }
        }
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            cookie.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
        }
        if (login) {
            System.out.println("DivShare Login successful :)");
        } else {
            System.out.println("DivShare Login failed :(");
        }
        System.out.println("Cookie : " + cookie.toString());
    }
