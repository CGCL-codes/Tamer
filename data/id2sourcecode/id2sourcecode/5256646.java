    private static void loginUploadingdotcom() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to uploading.com");
        HttpPost httppost = new HttpPost("http://uploading.com/general/login_form/?SID=" + sidcookie.replace("SID=", "") + "&JsHttpRequest=" + new Date().getTime() + "0-xml");
        httppost.setHeader("Referer", "http://www.uploading.com/");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        httppost.setHeader("Cookie", sidcookie + ";" + cachecookie + ";" + timecookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email", uname));
        formparams.add(new BasicNameValuePair("password", pwd));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity en = httpresponse.getEntity();
        uploadresponse = EntityUtils.toString(en);
        System.out.println("Upload response : " + uploadresponse);
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("u")) {
                ucookie = escookie.getName() + "=" + escookie.getValue();
                System.out.println(ucookie);
                login = true;
            }
            if (escookie.getName().equalsIgnoreCase("cache")) {
                cachecookie = escookie.getName() + "=" + escookie.getValue();
                System.out.println(cachecookie);
            }
            if (escookie.getName().equalsIgnoreCase("time")) {
                timecookie = escookie.getName() + "=" + escookie.getValue();
                System.out.println(timecookie);
            }
        }
        if (login) {
            System.out.println("Uploading.com Login successful. :)");
            afterloginpage = getData();
            uploadinglink = parseResponse(afterloginpage, "upload_url\":\"", "\"");
            uploadinglink = uploadinglink.replaceAll("\\\\", "");
            System.out.println("New Upload link : " + uploadinglink);
            postURL = uploadinglink;
            sid = parseResponse(afterloginpage, "SID: '", "'");
            System.out.println("New sid from site : " + sid);
        } else {
            login = false;
            System.out.println("Uploading.com Login failed");
        }
    }
