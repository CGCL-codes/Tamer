    private synchronized void loadCookie() {
        if (rapUrl == null) {
            return;
        }
        try {
            URLConnection urlCon = rapUrl.openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(urlCon.getOutputStream());
            String login = loginStorage.getLogin(LoginStorage.SERVICE_RAPIDSHARE).getLogin();
            String password = loginStorage.getLogin(LoginStorage.SERVICE_RAPIDSHARE).getPassword();
            output.writeBytes("login=" + login + "&password=" + password);
            output.flush();
            output.close();
            String headerName = null;
            for (int i = 1; (headerName = urlCon.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookieString = urlCon.getHeaderField(i);
                    Logger.getRootLogger().debug("cookie: " + cookieString);
                }
            }
        } catch (MalformedURLException e) {
            Logger.getRootLogger().error("bad url", e);
        } catch (IOException e) {
            Logger.getRootLogger().error("IO problem", e);
        }
    }
