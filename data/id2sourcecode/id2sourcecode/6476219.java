    private void loginUploadingdotcom() throws Exception {
        loginsuccessful = false;
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        NULogger.getLogger().info("Trying to log in to uploading.com");
        HttpPost httppost = new HttpPost("http://uploading.com/general/login_form/?SID=" + sidcookie.replace("SID=", "") + "&JsHttpRequest=" + new Date().getTime() + "0-xml");
        httppost.setHeader("Referer", "http://www.uploading.com/");
        httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        httppost.setHeader("Cookie", sidcookie + ";" + cachecookie + ";" + timecookie);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("email", getUsername()));
        formparams.add(new BasicNameValuePair("password", getPassword()));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity en = httpresponse.getEntity();
        uploadresponse = EntityUtils.toString(en);
        NULogger.getLogger().log(Level.INFO, "Upload response : {0}", uploadresponse);
        NULogger.getLogger().info("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            if (escookie.getName().equalsIgnoreCase("u")) {
                ucookie = escookie.getName() + "=" + escookie.getValue();
                NULogger.getLogger().info(ucookie);
                loginsuccessful = true;
            }
            if (escookie.getName().equalsIgnoreCase("cache")) {
                cachecookie = escookie.getName() + "=" + escookie.getValue();
                NULogger.getLogger().info(cachecookie);
            }
            if (escookie.getName().equalsIgnoreCase("time")) {
                timecookie = escookie.getName() + "=" + escookie.getValue();
                NULogger.getLogger().info(timecookie);
            }
        }
        if (loginsuccessful) {
            NULogger.getLogger().info("Uploading.com Login successful. :)");
            loginsuccessful = true;
            username = getUsername();
            password = getPassword();
            HostsPanel.getInstance().uploadingDotComCheckBox.setEnabled(true);
        } else {
            NULogger.getLogger().info("Uploading.com Login failed :(");
            loginsuccessful = false;
            username = "";
            password = "";
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
