    protected byte[] getDigestSHAwithoutLast() {
        synchronized (sha) {
            sha.update(buffer, 0, marked_pos);
            return sha.digest();
        }
    }
