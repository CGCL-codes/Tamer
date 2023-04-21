    public static byte[] cryptPassword(byte[] password) throws NoSuchAlgorithmException {
        if (MESSAGE_DIGEST == null) MESSAGE_DIGEST = MessageDigest.getInstance(PASSWORD_ENCRYPTION_ALGORITHM);
        byte ret[] = MESSAGE_DIGEST.digest(password);
        MESSAGE_DIGEST.reset();
        return ret;
    }
