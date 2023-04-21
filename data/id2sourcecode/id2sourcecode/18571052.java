    public static ByteBlock getMD5(ByteBlock block) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException impossible) {
            throw new UnknownError("no such algorithm: MD5");
        }
        return ByteBlock.wrap(digest.digest(block.toByteArray()));
    }
