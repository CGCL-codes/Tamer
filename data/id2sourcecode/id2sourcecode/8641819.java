    public static byte[] computeMD5Hash(byte[] data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hash = messageDigest.digest(data);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }
