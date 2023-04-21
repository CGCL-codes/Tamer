    public static byte[] getMD5HashDigest(byte[] input) throws ApplicationException {
        try {
            return MessageDigest.getInstance("MD5").digest(input);
        } catch (Exception e) {
            throw new ApplicationException(e, "Exception creating MD5 hash digest");
        }
    }
