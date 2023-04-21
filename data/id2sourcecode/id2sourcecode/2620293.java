    public static String sha1(byte[] data) {
        sha1Digest.reset();
        return toHexString(sha1Digest.digest(data));
    }
