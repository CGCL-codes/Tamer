    public static byte[] sha1(ByteBuffer[] buffers) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            for (ByteBuffer buffer : buffers) {
                update(digest, buffer.slice());
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }
