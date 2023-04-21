    public static String generateMD5(byte[] HashBytes) {
        MessageDigest MDigest = null;
        try {
            MDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MDigest.update(HashBytes);
        return ByteConversion.convertToHex(MDigest.digest());
    }
