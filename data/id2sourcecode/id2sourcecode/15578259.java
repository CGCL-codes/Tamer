    private synchronized long hashCode(final byte[] bytes, final byte additional) {
        md5.reset();
        md5.update(additional);
        final byte[] digest = md5.digest(bytes);
        final ByteBuffer bb = ByteBuffer.wrap(digest);
        return bb.getLong();
    }
