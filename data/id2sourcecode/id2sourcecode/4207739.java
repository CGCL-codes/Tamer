    @Override
    public synchronized byte[] createMd5Checksum(final byte[] in) {
        digest.reset();
        digest.update(in);
        byte[] bytes = digest.digest();
        return bytes;
    }
