    public byte[] getMD() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        return md.digest(code);
    }
