    public void login() {
        try {
            initialize();
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            NULogger.getLogger().info("Trying to log in to Giga Size");
            HttpPost httppost = new HttpPost("http://www.gigasize.com/signin");
            httppost.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("func", ""));
            formparams.add(new BasicNameValuePair("token", formtoken));
            formparams.add(new BasicNameValuePair("signRem", "1"));
            formparams.add(new BasicNameValuePair("email", getUsername()));
            formparams.add(new BasicNameValuePair("password", getPassword()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(entity);
            HttpResponse httpresponse = httpclient.execute(httppost);
            NULogger.getLogger().info("Getting cookies........");
            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
            Cookie escookie = null;
            gigasizecookies.setLength(0);
            while (it.hasNext()) {
                escookie = it.next();
                gigasizecookies.append(escookie.getName()).append("=").append(escookie.getValue()).append(";");
            }
            NULogger.getLogger().info(gigasizecookies.toString());
            if (gigasizecookies.toString().contains("MIIS_GIGASIZE_AUTH")) {
                loginsuccessful = true;
                username = getUsername();
                password = getPassword();
                NULogger.getLogger().info("GigaSize Login Success");
            } else {
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
                NULogger.getLogger().info("GigaSize Login failed");
            }
        } catch (Exception e) {
            NULogger.getLogger().log(Level.SEVERE, "{0}: {1}", new Object[] { getClass().getName(), e.toString() });
            System.err.println(e);
        }
    }
