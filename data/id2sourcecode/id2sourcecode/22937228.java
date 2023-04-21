    public static String getSHA1HashAsHexEncodedString(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashSHA1 = messageDigest.digest(bytes);
            return hexEncodedStringForByteArray(hashSHA1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
