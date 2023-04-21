    public void login() {
        loginsuccessful = false;
        try {
            cookies = new StringBuilder();
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to fileden.com");
            HttpPost httppost = new HttpPost("http://www.fileden.com/account.php?action=login");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("action", "login"));
            formparams.add(new BasicNameValuePair("task", "login"));
            formparams.add(new BasicNameValuePair("username", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                cookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
            }
            if (cookies.toString().contains("uploader_username")) {
                loginsuccessful = true;
            }
            if (loginsuccessful) {
                NULogger.getLogger().info("FileDen Login success :)");
                NULogger.getLogger().info(cookies.toString());
                HostsPanel.getInstance().fileDenCheckBox.setEnabled(true);
                username = getUsername();
                password = getPassword();
            } else {
                NULogger.getLogger().info("FileDen Login failed :(");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in FileDen Login", getClass().getName());
        }
    }
