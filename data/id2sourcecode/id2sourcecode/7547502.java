    public static byte[] hash(byte[] array) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.update(array);
            return sha1.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
