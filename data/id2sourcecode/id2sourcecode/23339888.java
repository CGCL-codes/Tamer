    public String getAccountHash() throws GeneralSecurityException {
        String username = user.getUsername();
        String pwd = user.getPwd();
        String sha = username + "|" + pwd;
        byte[] b = MessageDigest.getInstance("SHA-1").digest(sha.getBytes());
        sha = hash(b);
        return sha;
    }
