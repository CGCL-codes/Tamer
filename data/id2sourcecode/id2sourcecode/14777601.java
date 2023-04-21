    public static byte[] encrypt(String string, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] bytesArray = string.getBytes();
        return messageDigest.digest(bytesArray);
    }
