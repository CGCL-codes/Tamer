    public static String encodePassword(String password, String algorithm) {
        if (password == null) return null;
        MessageDigest passwdSignature = null;
        try {
            passwdSignature = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            log.fatal("encode password exception", nsae);
            return null;
        }
        byte[] buffer = passwdSignature.digest(password.getBytes());
        String encPwd = algorithm + "/" + JSTKUtil.hexStringFromBytes(buffer);
        if (log.isDebugEnabled()) log.debug("Encoded password is " + encPwd);
        return encPwd;
    }
