    private static String getHashKey(String entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Parameter entry must not be null!");
        }
        if (entry.getBytes() == null || entry.getBytes().length == 0) {
            throw new IllegalArgumentException("Byte representation of Parameter " + " must not be null or have length 0!");
        }
        byte[] testBytes = entry.getBytes();
        synchronized (messageDigest) {
            messageDigest.reset();
            messageDigest.update(testBytes);
            byte[] id = messageDigest.digest();
            return toHexString(id, getLengthOfIDsInBytes());
        }
    }
