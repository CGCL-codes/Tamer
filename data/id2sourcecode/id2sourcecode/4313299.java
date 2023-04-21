    private static byte[] computeChecksum(byte[] data) {
        if (data == null) {
            throw new NullPointerException("illegal data");
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
