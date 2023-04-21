    public byte[] digest(byte[] rawInfo) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1_ALGORITHM);
            md.update(rawInfo);
            byte[] cipher = md.digest();
            return cipher;
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
