    public static String createPasswordHash(String password, byte[] salt) {
        String passHash = null;
        try {
            byte[] passBytes = password.getBytes();
            MessageDigest md = MessageDigest.getInstance(DEFAULT_ALGORITHM);
            if (salt != null) {
                md.update(salt);
            }
            byte[] hashBytes = md.digest(passBytes);
            passHash = bytesToBase64(hashBytes);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Password hash calculation failed ", e);
            }
        }
        return passHash;
    }
