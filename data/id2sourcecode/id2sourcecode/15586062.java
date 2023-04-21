    @Override
    public void login() {
        try {
            loginsuccessful = false;
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient eshttpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to EasyShare");
            HttpPost httppost = new HttpPost("http://www.easy-share.com/accounts/login");
            httppost.setHeader("Referer", "http://www.easy-share.com/");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("login", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            formparams.add(new BasicNameValuePair("remember", "1"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = eshttpclient.execute(httppost);
            if (httpresponse.getFirstHeader("Set-Cookie") == null) {
                NULogger.getLogger().info("Easy-share login not successful");
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            } else {
                Iterator<Cookie> it = eshttpclient.getCookieStore().getCookies().iterator();
                Cookie escookie;
                while (it.hasNext()) {
                    escookie = it.next();
                    if (escookie.getName().equals("logacc") && escookie.getValue().equals("1")) {
                        NULogger.getLogger().info("EasyShare login successful");
                        loginsuccessful = true;
                        username = getUsername();
                        password = getPassword();
                        break;
                    }
                }
                if (!loginsuccessful) {
                    JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                    AccountsManager.getInstance().setVisible(true);
                }
            }
            EntityUtils.consume(httpresponse.getEntity());
        } catch (Exception ex) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: Error in $EasyShare Login", getClass().getName());
        }
    }
