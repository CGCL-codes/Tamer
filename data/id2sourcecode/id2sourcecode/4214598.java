    public UserProfileImpl authenticate(UserAccessorImpl psswAccessor) {
        if (username == null || username.length() == 0 || passwordDigest == null || passwordDigest.length() == 0) return null;
        UserProfileImpl user = psswAccessor.getUserByPassword(username);
        if (user == null || user.getPassword() == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            String pswdStr = nonce + created + user.getPassword();
            String userPasswordDigest = new String(md.digest(pswdStr.getBytes()));
            if (userPasswordDigest.equalsIgnoreCase(passwordDigest)) return user;
        } catch (NoSuchAlgorithmException x) {
            logger.error("Unable to authenticate. SHA-1 algorithm is not present", x);
        }
        return null;
    }
