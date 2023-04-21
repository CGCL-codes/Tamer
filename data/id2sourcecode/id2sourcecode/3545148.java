    public User updateUser(String userId, String password1, String password2, boolean passwordReset) throws PortalException, SystemException {
        userId = userId.trim().toLowerCase();
        validate(userId, password1, password2);
        User user = UserUtil.findByPrimaryKey(userId);
        String oldEncPwd = user.getPassword();
        if (!user.isPasswordEncrypted()) {
            oldEncPwd = Encryptor.digest(user.getPassword());
        }
        String newEncPwd = Encryptor.digest(password1);
        int passwordsLifespan = GetterUtil.getInteger(PropsUtil.get(PropsUtil.PASSWORDS_LIFESPAN));
        Date expirationDate = null;
        if (passwordsLifespan > 0) {
            expirationDate = new Date(System.currentTimeMillis() + Time.DAY * passwordsLifespan);
        }
        if (user.hasCompanyMx()) {
            MailManagerUtil.updatePassword(userId, password1);
        }
        user.setPassword(newEncPwd);
        user.setPasswordEncrypted(true);
        user.setPasswordExpirationDate(expirationDate);
        user.setPasswordReset(passwordReset);
        UserUtil.update(user);
        PasswordTrackerLocalManagerUtil.trackPassword(userId, oldEncPwd);
        return user;
    }
