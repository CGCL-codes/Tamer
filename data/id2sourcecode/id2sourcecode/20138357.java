    protected byte[] getDigestSHA() {
        synchronized (sha) {
            int len = (read_pos_end > write_pos) ? read_pos_end : write_pos;
            sha.update(buffer, 0, len);
            return sha.digest();
        }
    }
