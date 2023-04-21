    public static final byte[] passwdCrypt(byte[] pwd) {
        if (useFastMd5) {
            return MD5.passwdCrypt(pwd);
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(DigestAlgo.MD5.name);
        } catch (NoSuchAlgorithmException e) {
            return MD5.passwdCrypt(pwd);
        }
        for (int i = 0; i < 16; i++) {
            digest.update(pwd, 0, pwd.length);
            digest.update(salt, 0, salt.length);
        }
        byte[] buf = digest.digest();
        digest = null;
        return buf;
    }
