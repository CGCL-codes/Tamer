    public String changePassword() {
        try {
            String password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(originalPwd.getBytes())).toString(16);
            if (password.length() < 32) {
                for (int i = (32 - password.length()); i > 0; i--) {
                    password = "0" + password;
                }
            }
            String message;
            if (user.getPassword().equals(password)) {
                if (!pwd1.equals(pwd2)) {
                    message = UIMessenger.getMessage(this.userLocale, "application.login.passwordsDoNotMatch");
                    UIMessenger.addErrorMessage(message, "");
                    return StandardResults.FAIL;
                }
                password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(pwd1.getBytes())).toString(16);
                if (password.length() < 32) {
                    for (int i = (32 - password.length()); i > 0; i--) {
                        password = "0" + password;
                    }
                }
                user.setPassword(password);
                String ret = user.update();
                if (ret.equals(StandardResults.SUCCESS)) {
                    message = UIMessenger.getMessage(this.userLocale, "application.login.saved");
                    UIMessenger.addInfoMessage(message, "");
                }
            } else {
                message = UIMessenger.getMessage(this.userLocale, "application.login.wrongPassword");
                UIMessenger.addErrorMessage(message, "");
                return StandardResults.FAIL;
            }
            return StandardResults.SUCCESS;
        } catch (Exception ex) {
            SessionHolder.endSession();
            UIMessenger.addFatalKeyMessage("error.transaction.abort", getUserLocale());
            ex.printStackTrace();
            return StandardResults.FAIL;
        }
    }
