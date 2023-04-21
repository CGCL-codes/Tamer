    public void hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        byte[] passwordBytes = password.getBytes();
        byte[] hash = md.digest(passwordBytes);
        this.password = Util.encodeBase16(hash);
    }
