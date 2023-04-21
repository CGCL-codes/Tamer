    private static long getHash(byte[] buffer) throws NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance("SHA-1").digest(buffer);
        long hash = 0;
        int length = digest.length;
        if (length > 8) {
            length = 8;
        }
        for (int i = 0; i < length; i++) {
            hash += ((long) (digest[i] & 0xff)) << (i * 8);
        }
        return hash;
    }
