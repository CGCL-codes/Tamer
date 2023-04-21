    public void loginUGotFile() throws Exception {
        loginsuccessful = false;
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to ugotfile.com");
        HttpPost httppost = new HttpPost("http://ugotfile.com/user/login");
        httppost.setHeader("Cookie", phpsessioncookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("ugfLoginUserName", getUsername()));
        formparams.add(new BasicNameValuePair("ugfLoginPassword", getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        if (httpresponse.getStatusLine().toString().contains("302")) {
            tmp = httpresponse.getLastHeader("Location").getValue();
            NULogger.getLogger().info("UGotFile Login success");
            loginsuccessful = true;
            username = getUsername();
            password = getPassword();
        } else {
            NULogger.getLogger().info("UGotFile login failed");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
