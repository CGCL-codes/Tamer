    public AuthorizationBean_v03 doLocalLogin(LocalAuthorizationBean_v03 tempL, String password) {
        if (log.isDebugEnabled()) {
            log.debug("New Login: appid; " + tempL.getApplicationIdentifier() + " username; " + tempL.getUserIdentifier());
        }
        if (log.isDebugEnabled()) {
            log.debug("Found user: " + tempL);
        }
        String hashedPassword = "";
        try {
            hashedPassword = HashUtil.getInstance().digest(password);
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal("Error hashing password: ", e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Password user: " + tempL.getPassword() + " given: " + hashedPassword);
        }
        String logincount = "";
        if (tempL.getCustomAttributesMap() != null) {
            List<String> strList = tempL.getCustomAttributesMap().get("localaccount.logincount");
            if (strList != null && strList.size() > 0) {
                logincount = strList.get(0);
            }
        }
        int logincountI = 0;
        if (logincount != null && !logincount.equals("")) {
            logincountI = Integer.parseInt(logincount);
            logincountI = logincountI + 1;
        } else {
            logincountI = 1;
        }
        List lastlogin = new ArrayList<String>();
        lastlogin.add("" + System.currentTimeMillis());
        List countlist = new ArrayList<String>();
        countlist.add("" + logincountI);
        tempL.getCustomAttributesMap().put("localaccount.lastlogin", lastlogin);
        tempL.getCustomAttributesMap().put("localaccount.logincount", countlist);
        locMan.saveObject(tempL.getApplicationIdentifier() + "-" + tempL.getUserIdentifier(), tempL);
        if (log.isDebugEnabled()) {
            log.debug("User to give back: " + tempL.getPassword() + " -> " + tempL.getPassword().equals(hashedPassword));
        }
        if (tempL != null && tempL.getPassword() != null && tempL.getPassword().equals(hashedPassword)) {
            tempL.setPassword("");
            return tempL;
        }
        return null;
    }
