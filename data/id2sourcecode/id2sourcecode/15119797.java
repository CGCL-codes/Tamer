    public void login() {
        loginsuccessful = false;
        try {
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to oron.com");
            HttpPost httppost = new HttpPost("http://oron.com/login");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("login", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            formparams.add(new BasicNameValuePair("op", "login"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                if (escookie.getName().equalsIgnoreCase("login")) {
                    logincookie = "login=" + escookie.getValue();
                    NULogger.getLogger().info(logincookie);
                }
                if (escookie.getName().equalsIgnoreCase("xfss")) {
                    xfsscookie = "xfss=" + escookie.getValue();
                    NULogger.getLogger().info(xfsscookie);
                    loginsuccessful = true;
                }
            }
            if (loginsuccessful) {
                NULogger.getLogger().info("Oron Login successful :)");
                username = getUsername();
                password = getPassword();
            } else {
                NULogger.getLogger().info("Oron Login failed :(");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: {1}", new Object[] { getClass().getName(), e.toString() });
            System.err.println(e);
        }
    }
