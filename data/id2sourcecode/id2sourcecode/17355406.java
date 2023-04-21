    private byte[] md5(byte[] bytes) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalError("Can't get MD5 MessageDigest");
        }
        md.update(bytes);
        return md.digest();
    }
