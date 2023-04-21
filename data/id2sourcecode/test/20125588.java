    public static byte[] getHash(byte[] message) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            return md.digest(message);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }
