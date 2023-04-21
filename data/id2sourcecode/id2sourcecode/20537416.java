    public static void loginIFile() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to ifile.it");
        HttpPost httppost = new HttpPost("https://secure.ifile.it/api-fetch_apikey.api");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "007007dinesh"));
        formparams.add(new BasicNameValuePair("password", ""));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        loginresponse = EntityUtils.toString(httpresponse.getEntity());
        System.out.println("response : " + loginresponse);
        if (loginresponse.contains("akey")) {
            apikey = parseResponse(loginresponse, "akey\":\"", "\"");
            System.out.println("IFile Login sccuess :)");
            System.out.println("API key : " + apikey);
        } else {
            System.out.println("IFile login failed :(");
        }
    }
