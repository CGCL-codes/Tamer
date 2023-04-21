    public static String generateSHA512(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }
