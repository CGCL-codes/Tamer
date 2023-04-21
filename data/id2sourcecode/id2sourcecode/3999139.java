    public void digest(boolean reset, byte[] buffer, int offset) {
        if (reset != true) throw new UnsupportedOperationException();
        try {
            digest.digest(buffer, offset, digest.getDigestLength());
        } catch (DigestException e) {
            throw new IllegalStateException(e.toString());
        }
    }
