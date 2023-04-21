    protected String getAvatarId(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-1");
        byte[] hash = md.digest(data);
        return StringUtils.encodeHex(hash);
    }
