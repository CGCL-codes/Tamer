    private static byte[] createChecksum(String str) throws NoSuchAlgorithmException {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.reset();
        return complete.digest(str.getBytes());
    }
