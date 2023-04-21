    public static byte[] makeDigest(String message) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        return sha.digest(message.getBytes());
    }
