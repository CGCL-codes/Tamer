    public void loginDropBox() throws Exception {
        loginsuccessful = false;
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to dropbox.com");
        HttpPost httppost = new HttpPost("https://www.dropbox.com/login");
        httppost.setHeader("Cookie", gvccookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("login_email", getUsername()));
        formparams.add(new BasicNameValuePair("login_password", getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        NULogger.getLogger().info("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("forumjar")) {
                forumjarcookie = "forumjar=" + escookie.getValue();
                loginsuccessful = true;
                NULogger.getLogger().info(forumjarcookie);
            }
            if (escookie.getName().equalsIgnoreCase("touch")) {
                touchcookie = "touch=" + escookie.getValue();
                NULogger.getLogger().info(touchcookie);
            }
            if (escookie.getName().equalsIgnoreCase("forumlid")) {
                forumlidcookie = "forumlid=" + escookie.getValue();
                NULogger.getLogger().info(forumlidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("lid")) {
                lidcookie = "lid=" + escookie.getValue();
                NULogger.getLogger().info(lidcookie);
            }
            if (escookie.getName().equalsIgnoreCase("jar")) {
                jarcookie = "jar=" + escookie.getValue();
                NULogger.getLogger().info(jarcookie);
            }
        }
        if (loginsuccessful) {
            loginsuccessful = true;
            HostsPanel.getInstance().dropBoxCheckBox.setEnabled(true);
            username = getUsername();
            password = getPassword();
            NULogger.getLogger().info("DropBox Login success :)");
        } else {
            NULogger.getLogger().log(Level.SEVERE, "DropBox Login failed :(");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
