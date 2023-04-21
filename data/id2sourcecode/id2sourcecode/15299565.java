    public byte[] sha(byte[] clearText) {
        MessageDigest md;
        byte[] digest;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(clearText);
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e.toString());
        }
        return digest;
    }
