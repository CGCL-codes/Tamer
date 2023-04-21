    protected static String getSaltedPassword(byte[] password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(password);
        byte[] hash = digest.digest(salt);
        byte[] all = new byte[hash.length + salt.length];
        for (int i = 0; i < hash.length; i++) {
            all[i] = hash[i];
        }
        for (int i = 0; i < salt.length; i++) {
            all[hash.length + i] = salt[i];
        }
        byte[] base64 = Base64.encodeBase64(all);
        String saltedString = null;
        try {
            saltedString = SSHA + new String(base64, "UTF8");
        } catch (UnsupportedEncodingException e) {
            log.log(Level.SEVERE, "You do not have UTF-8!?!");
        }
        return saltedString;
    }
