    private static byte[] calculateHash(byte[] historyHash, byte[] appliedDeltaBytes) {
        byte[] joined = new byte[appliedDeltaBytes.length + historyHash.length];
        byte[] result = new byte[hashSizeBits / 8];
        System.arraycopy(historyHash, 0, joined, 0, historyHash.length);
        System.arraycopy(appliedDeltaBytes, 0, joined, historyHash.length, appliedDeltaBytes.length);
        try {
            historyHash = MessageDigest.getInstance("SHA-256").digest(joined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(historyHash, 0, result, 0, result.length);
        return result;
    }
