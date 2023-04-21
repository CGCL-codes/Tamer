    private static ByteString nextHash(ByteString historyHash, ByteString appliedDeltaBytes) {
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sha256.update(historyHash.toByteArray());
        return ByteString.copyFrom(sha256.digest(appliedDeltaBytes.toByteArray()), 0, HASH_SIZE_BITS / 8);
    }
