    public void loginBox() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().log(Level.INFO, "{0}Trying to log in to box.com", getClass());
        HttpPost httppost = new HttpPost("https://www.box.net/api/1.0/auth/" + ticket);
        httppost.setHeader("Cookie", zcookie + ";" + visitorcookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login", getUsername()));
        formparams.add(new BasicNameValuePair("password", getPassword()));
        formparams.add(new BasicNameValuePair("__login", "1"));
        formparams.add(new BasicNameValuePair("dologin", "1"));
        formparams.add(new BasicNameValuePair("reg_step", ""));
        formparams.add(new BasicNameValuePair("submit1", "1"));
        formparams.add(new BasicNameValuePair("folder", ""));
        formparams.add(new BasicNameValuePair("skip_framework_login", "1"));
        formparams.add(new BasicNameValuePair("login_or_register_mode", "login"));
        formparams.add(new BasicNameValuePair("new_login_or_register_mode", ""));
        formparams.add(new BasicNameValuePair("request_token", request_token));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        NULogger.getLogger().log(Level.INFO, "{0}Gonna print the response", getClass());
        loginresponse = EntityUtils.toString(httpresponse.getEntity());
        if (loginresponse.contains("Invalid username or password")) {
            NULogger.getLogger().log(Level.INFO, "{0}Box login failed", getClass());
            loginsuccessful = false;
            NeembuuUploaderProperties.setEncryptedProperty(KEY_AUTH_TOKEN, "");
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        } else {
            NULogger.getLogger().log(Level.INFO, "{0}Box login successful :)", getClass());
            getUserInfo();
            loginsuccessful = true;
            HostsPanel.getInstance().boxDotComCheckBox.setEnabled(true);
            username = getUsername();
            password = getPassword();
            NULogger.getLogger().info("Box Login success :)");
        }
    }
