    public void login() {
        loginsuccessful = false;
        try {
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to localhostr");
            HttpPost httppost = new HttpPost("http://localhostr.com/signin");
            httppost.setHeader("Referer", "http://www.localhostr.com/");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            NULogger.getLogger().info(httpresponse.getStatusLine().toString());
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            while (it.hasNext()) {
                escookie = it.next();
                if (escookie.getName().contains("session")) {
                    sessioncookie = escookie.getName() + " = " + escookie.getValue();
                    NULogger.getLogger().log(Level.INFO, "session cookie : {0}", sessioncookie);
                }
            }
            if (httpresponse.getStatusLine().getStatusCode() == 302) {
                loginsuccessful = true;
                NULogger.getLogger().info("localhostr Login Success");
                username = getUsername();
                password = getPassword();
            } else {
                NULogger.getLogger().info("localhostr Login failed");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            }
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
        }
    }
