    public byte[] getDigestBytes() throws Exception {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            return sha.digest(getCertBytes());
        } catch (java.lang.Exception e) {
            throw new Exception(e);
        }
    }
