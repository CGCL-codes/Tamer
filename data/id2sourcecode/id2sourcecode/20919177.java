    public boolean isValidPassword(String userId, String password) throws PortalException, SystemException {
        int passwordsRecycle = GetterUtil.getInteger(PropsUtil.get(PropsUtil.PASSWORDS_RECYCLE));
        if (passwordsRecycle > 0) {
            String newEncPwd = Encryptor.digest(password);
            User user = UserUtil.findByPrimaryKey(userId);
            String oldEncPwd = user.getPassword();
            if (!user.isPasswordEncrypted()) {
                oldEncPwd = Encryptor.digest(user.getPassword());
            }
            if (oldEncPwd.equals(newEncPwd)) {
                return false;
            }
            Date now = new Date();
            Iterator itr = PasswordTrackerUtil.findByUserId(userId).iterator();
            while (itr.hasNext()) {
                PasswordTracker passwordTracker = (PasswordTracker) itr.next();
                Date recycleDate = new Date(passwordTracker.getCreateDate().getTime() + Time.DAY * passwordsRecycle);
                if (recycleDate.after(now)) {
                    if (passwordTracker.getPassword().equals(newEncPwd)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
