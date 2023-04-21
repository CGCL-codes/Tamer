    public static byte[] hash(final byte[] hashThis) {
        byte[] returnVal = new byte[0];
        try {
            returnVal = MessageDigest.getInstance("SHA-1").digest(hashThis);
        } catch (NoSuchAlgorithmException nsae) {
        }
        return returnVal;
    }
