    public static final String toHexMD5(byte[] toencode) {
        if (toencode == null) throw new NullPointerException(MessageLocalizer.getMessage("STRING_TO_ENCODE_CANNOT_BE_NULL"));
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return toHexString(digest.digest(toencode));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(MessageLocalizer.getMessage("ERROR_ENCODING_TO_MD5") + e.getMessage());
        }
    }
