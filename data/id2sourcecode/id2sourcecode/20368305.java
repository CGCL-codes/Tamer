    public static final String passwdCrypt(String pwd) {
        if (useFastMd5) {
            return MD5.passwdCrypt(pwd);
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(DigestAlgo.MD5.name);
        } catch (NoSuchAlgorithmException e) {
            return MD5.passwdCrypt(pwd);
        }
        byte[] bpwd = pwd.getBytes();
        for (int i = 0; i < 16; i++) {
            digest.update(bpwd, 0, bpwd.length);
            digest.update(salt, 0, salt.length);
        }
        byte[] buf = digest.digest();
        digest = null;
        return getHex(buf);
    }
