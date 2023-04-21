    public static byte[] secureHash(byte[] data) throws Exception {
        MessageDigest shaDigest = MessageDigest.getInstance("SHA");
        return shaDigest.digest(data);
    }
