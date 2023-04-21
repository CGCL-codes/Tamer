    public void loginLetitbit() throws Exception {
        loginsuccessful = false;
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to letitbit.com");
        HttpPost httppost = new HttpPost("http://letitbit.net/");
        httppost.setHeader("Cookie", phpsessioncookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("act", "login"));
        formparams.add(new BasicNameValuePair("login", getUsername()));
        formparams.add(new BasicNameValuePair("password", getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        NULogger.getLogger().info("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("log")) {
                logcookie = "log=" + escookie.getValue();
                NULogger.getLogger().info(logcookie);
                loginsuccessful = true;
            }
            if (escookie.getName().equalsIgnoreCase("pas")) {
                pascookie = "pas=" + escookie.getValue();
                NULogger.getLogger().info(pascookie);
            }
            if (escookie.getName().equalsIgnoreCase("host")) {
                hostcookie = "host=" + escookie.getValue();
                NULogger.getLogger().info(hostcookie);
            }
        }
        if (loginsuccessful) {
            loginsuccessful = true;
            username = getUsername();
            password = getPassword();
            NULogger.getLogger().info("Letitbit.net Login success :)");
        } else {
            NULogger.getLogger().info("Letitbit.net Login failed :(");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
