    public void login() {
        loginsuccessful = false;
        try {
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to Zoho Docs");
            HttpPost httppost = new HttpPost("https://accounts.zoho.com/login");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("LOGIN_ID", getUsername()));
            formparams.add(new BasicNameValuePair("PASSWORD", getPassword()));
            formparams.add(new BasicNameValuePair("IS_AJAX", "true"));
            formparams.add(new BasicNameValuePair("remember", "-1"));
            formparams.add(new BasicNameValuePair("servicename", "ZohoPC"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                zohodocscookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
                NULogger.getLogger().info(zohodocscookies.toString());
            }
            if (zohodocscookies.toString().contains(getUsername())) {
                loginsuccessful = true;
            }
            if (loginsuccessful) {
                NULogger.getLogger().info("Zoho Docs Login Success");
                HostsPanel.getInstance().zohoDocsCheckBox.setEnabled(true);
                username = getUsername();
                password = getPassword();
            } else {
                NULogger.getLogger().info("Zoho Docs Login failed");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in ZohoDocs Login", getClass().getName());
        }
    }
