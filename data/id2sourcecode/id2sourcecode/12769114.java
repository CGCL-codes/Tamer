    public String addPerson() {
        if (!userHandler.isOwner()) return StandardResults.FAIL;
        try {
            String message;
            if ((userHandler.getUser().getLoginLevel() == null || userHandler.getUser().getLoginLevel() != 0) && this.getCompanyId() == 0) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.unsaved");
                UIMessenger.addInfoMessage(message, "");
                return StandardResults.FAIL;
            }
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
            person.setCompany(this.getCompanyId());
            String lang = ParameterAccess.getLanguages().get(getLanguage()).toLowerCase();
            person.setLanguage(lang);
            Session sess = SessionHolder.currentSession().getSess();
            sess.evict(this.person);
            int i = (Integer) sess.createQuery("SELECT count(*) FROM " + Person.class.getName() + " e WHERE e.loginCode = ? ").setString(0, person.getLoginCode()).iterate().next();
            if (i > 0) {
                message = UIMessenger.getMessage(userHandler.getUserLocale(), "application.login.loginExists");
                UIMessenger.addErrorMessage(message, "");
                return StandardResults.FAIL;
            }
            person.setLastLogin(new Date());
            if (!person.add().equals(StandardResults.SUCCESS)) {
                SessionHolder.endSession();
                UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
                return StandardResults.FAIL;
            }
            this.person = new Person();
            return StandardResults.SUCCESS;
        } catch (Exception ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler.getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }
