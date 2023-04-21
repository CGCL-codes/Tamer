    public static byte[] MD5(byte[]... strings) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            for (byte[] string : strings) {
                digest.update(string);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
