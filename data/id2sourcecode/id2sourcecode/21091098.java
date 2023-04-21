    public String login() {
        try {
            char[] pwdMD5 = Hex.encodeHex(MessageDigest.getInstance("MD5").digest(user.getPassword().getBytes()));
            String password = new String(pwdMD5);
            if (password.length() < 32) {
                for (int i = (32 - password.length()); i > 0; i--) {
                    password = "0" + password;
                }
            }
            Query q = SessionHolder.currentSession().getSess().createQuery(" FROM " + Person.class.getName() + "  WHERE lower(loginCode) = lower(?) AND password = ? ");
            q.setString(0, user.getLoginCode().trim());
            q.setString(1, password.trim());
            List l = q.list();
            if (q.list().isEmpty()) {
                loggedIn = false;
                user.setName(null);
                user.setPassword(null);
                if (this.userLocale == null) this.userLocale = Locale.getDefault();
                String message = UIMessenger.getMessage(this.userLocale, "application.login.error");
                UIMessenger.addErrorMessage(message, "");
                return StandardResults.FAIL;
            } else {
                loggedIn = true;
                user = (Person) l.get(0);
                lastLogin = user.getLastLogin();
                user.setLastLogin(new Date());
                user.update();
                this.userLocale = ParameterAccess.getLocale(user.getLanguage());
                new ParameterAccess().setLanguage(user.getLanguage());
                email = user.getEmail();
                phone = user.getPhoneNo();
                for (Entry<Integer, String> c : ParameterAccess.getLanguages().entrySet()) {
                    if (c.getValue().equals(user.getLanguage())) this.setLanguage(c.getKey());
                }
                restoreFilter();
                if (user.getCompany() == Company.OWNER || user.getLoginLevel() == Person.PARTNER) {
                    return StandardResults.INTRANET;
                } else {
                    company = (Company) SessionHolder.currentSession().getSess().createQuery(" FROM " + Company.class.getName() + "  WHERE id = ?").setInteger(0, user.getCompany()).uniqueResult();
                    contract = (CompanyContract) SessionHolder.currentSession().getSess().createQuery(" FROM " + CompanyContract.class.getName() + "  WHERE company = ?").setInteger(0, user.getCompany()).uniqueResult();
                    return StandardResults.SUCCESS;
                }
            }
        } catch (Exception ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }
