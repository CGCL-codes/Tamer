    public byte[] getDigest(String username, String password) {
        md.reset();
        md.update(username.toUpperCase().getBytes());
        md.update(password.getBytes());
        return md.digest();
    }
