    private static ByteBlock computeIconHash(ByteBlock iconData) {
        ByteBlock hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            hash = ByteBlock.wrap(digest.digest(iconData.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return hash;
    }
