    public void login() {
        loginsuccessful = false;
        try {
            initialize();
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to slingfile.com");
            HttpPost httppost = new HttpPost("http://www.slingfile.com/login");
            httppost.setHeader("Cookie", slingfilecookie.toString() + ";signupreferrerurl=http%3A%2F%2Fwww.slingfile.com%2F;");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("f_user", getUsername()));
            formparams.add(new BasicNameValuePair("f_password", getPassword()));
            formparams.add(new BasicNameValuePair("submit", "Login »"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info(httpresponse.getStatusLine().toString());
            Header lastHeader = httpresponse.getLastHeader("Location");
            if (lastHeader != null && lastHeader.getValue().contains("dashboard")) {
                loginsuccessful = true;
                slingfilecookie.append(";signupreferrerurl=http%3A%2F%2Fwww.slingfile.com%2F;");
                username = getUsername();
                password = getPassword();
                HostsPanel.getInstance().slingFileCheckBox.setEnabled(true);
            } else {
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
