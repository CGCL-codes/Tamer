    public static synchronized String calculateHash(String text) {
        byte[] hash = digest.digest(text.getBytes());
        return bytesToHex(hash);
    }
