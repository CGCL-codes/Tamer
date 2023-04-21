    final byte[] digest(byte[] buf, int offset, int size) {
        if (ivMessageDigest == null) {
            try {
                ivMessageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException(ex);
            }
        } else ivMessageDigest.reset();
        if (LOGGER.isDebugEnabled()) {
            long tt = System.currentTimeMillis();
            ivMessageDigest.update(buf, offset, size);
            byte[] digest = ivMessageDigest.digest();
            ivDigestElapsedTime += (System.currentTimeMillis() - tt);
            return digest;
        }
        ivMessageDigest.update(buf, offset, size);
        byte[] digest = ivMessageDigest.digest();
        return digest;
    }
