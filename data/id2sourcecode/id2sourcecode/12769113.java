    public String updatePerson() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            String message;
            if ((userHandler.getUser().getLoginLevel() == null || userHandler.getUser().getLoginLevel() != 0) && person.getId() != userHandler.getUser().getId()) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.unsaved");
                UIMessenger.addInfoMessage(message, "");
                return StandardResults.FAIL;
            }
            if (pwd1 != null && !pwd1.equals("")) {
                if (!pwd1.equals(pwd2)) {
                    message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.passwordsDoNotMatch");
                    UIMessenger.addErrorMessage(message, "");
                    return StandardResults.FAIL;
                }
                String password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(pwd2.getBytes())).toString(16);
                person.setPassword("");
                if (password.length() < 32) {
                    for (int i = (32 - password.length()); i > 0; i--) {
                        password = "0" + password;
                    }
                }
                person.setPassword(password);
            }
            String lang = ParameterAccess.getLanguages().get(getLanguage()).toLowerCase();
            person.setLanguage(lang);
            if (person.isLoginChange()) {
                Session sess = SessionHolder.currentSession().getSess();
                sess.evict(this.person);
                int i = (Integer) sess.createQuery("SELECT count(*) FROM " + Person.class.getName() + " e WHERE e.loginCode = ? ").setString(0, person.getLoginCode()).iterate().next();
                if (i > 0) {
                    message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.loginExists");
                    UIMessenger.addErrorMessage(message, "");
                    return StandardResults.FAIL;
                } else {
                    this.person.setLoginChange(false);
                }
            }
            if (!person.update().equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                return StandardResults.FAIL;
            }
            return StandardResults.SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }
