    public void login() {
        loginsuccessful = false;
        try {
            initialize();
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to netload.in");
            HttpPost httppost = new HttpPost("http://netload.in/index.php");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("txtuser", getUsername()));
            formparams.add(new BasicNameValuePair("txtpass", getPassword()));
            formparams.add(new BasicNameValuePair("txtcheck", "login"));
            formparams.add(new BasicNameValuePair("txtlogin", "Login"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                if (escookie.getName().equalsIgnoreCase("cookie_user")) {
                    usercookie = "cookie_user=" + escookie.getValue();
                    NULogger.getLogger().info(usercookie);
                    loginsuccessful = true;
                    username = getUsername();
                    password = getPassword();
                    NULogger.getLogger().info("Netload login successful :)");
                }
            }
            if (!loginsuccessful) {
                NULogger.getLogger().info("Netload Login failed :(");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: {1}", new Object[] { getClass().getName(), e.toString() });
        }
    }
