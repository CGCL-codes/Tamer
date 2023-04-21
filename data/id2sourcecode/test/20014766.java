    private String hash(String password) {
        if (password == null) {
            return null;
        }
        String ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] pwdhash = md.digest(password.trim().getBytes());
            ret = new String(Hex.encode(pwdhash));
        } catch (NoSuchAlgorithmException nsae) {
            log.error("SHA1 algorithm not supported.", nsae);
        }
        return ret;
    }
