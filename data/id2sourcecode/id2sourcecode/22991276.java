    public static final byte[] encryptMD5(byte decrypted[]) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(decrypted);
            byte hash[] = md5.digest();
            md5.reset();
            return hash;
        } catch (NoSuchAlgorithmException _ex) {
            return null;
        }
    }
