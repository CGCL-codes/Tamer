    public void addUser(String username, String password, SecurityRole role) throws IllegalArgumentException {
        if (username == null || username.length() == 0 || password == null || password.length() == 0 || role == null) throw new IllegalArgumentException("Cannot add a user with a null/zero-length username or password.");
        if (this.accounts.containsKey(username)) throw new IllegalArgumentException("Username \"" + username + "\" already exists.");
        MessageDigest md5digest;
        try {
            md5digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            this.server.io.logError("Could not generate password hash.");
            nsae.printStackTrace(this.server.io.getLog());
            throw new IllegalArgumentException("Could not generate password hash.");
        }
        byte[] pass_bytes = password.getBytes();
        byte[] hash_bytes = md5digest.digest(pass_bytes);
        User user = new User(username, hash_bytes, role);
        this.accounts.put(username, user);
    }
