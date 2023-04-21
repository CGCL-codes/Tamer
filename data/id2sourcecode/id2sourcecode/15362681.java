    private static String getTNPImpl(String userid, String password, String tpf, int count) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("getTNPImpl(String, String, String, int) - start");
        }
        String domain = "loginnet.passport.com";
        URL url0 = new URL("https://" + domain + "/login2.srf");
        HttpURLConnection con0 = (HttpURLConnection) url0.openConnection();
        con0.setRequestMethod("GET");
        con0.setUseCaches(false);
        con0.setDoInput(true);
        con0.setRequestProperty("Host", domain);
        String author = "Passport1.4 OrgVerb=GET,OrgURL=http://messenger.msn.com," + "sign-in=" + URLEncoder.encode(userid, "EUC-KR") + ",pwd=" + password + "," + tpf;
        con0.setRequestProperty("Authorization", author);
        String ret = getContent(con0.getInputStream(), con0.getContentLength());
        con0.disconnect();
        String auth = con0.getHeaderField("Authentication-Info");
        if (auth == null) return "t=0&p=0";
        String da_status = getValueFromKey(auth, "da-status");
        String fromPP = getValueFromKey(auth, "from-PP");
        if (logger.isDebugEnabled()) {
            logger.debug("getTNPImpl(String, String, String, int) - end");
        }
        return fromPP;
    }
