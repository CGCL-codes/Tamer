    public String login() throws AuthenticationException {
        logger.info("Launched LoginBean.login[" + username + "]");
        String passwordDigested = null;
        if (getPassword() != null) {
            try {
                passwordDigested = new String(Base64.encodeBase64(getCryptographyService().digest(getPassword().getBytes())));
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
        }
        getLoginService().loginByUsername(username, passwordDigested);
        return LOGIN_VIEW_ID;
    }
