    public User validateLogin(String username, String password) {
        if (username == null || username.length() == 0 || password == null || password.length() == 0) return null;
        MessageDigest md5digest;
        try {
            md5digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            this.server.io.logError("Could not generate password hash.");
            nsae.printStackTrace(this.server.io.getLog());
            return null;
        }
        byte[] hash_bytes = md5digest.digest(password.getBytes());
        md5digest.reset();
        User user = this.accounts.get(username);
        if (user == null) return null;
        if (hash_bytes.length != user.passhash.length) return null;
        for (int i = 0; i < hash_bytes.length; i++) if (hash_bytes[i] != user.passhash[i]) return null;
        return user;
    }
