    public static String generateSHA256(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }
