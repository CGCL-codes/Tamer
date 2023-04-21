    protected byte[] getDigestMD5() {
        synchronized (md5) {
            int len = (read_pos_end > write_pos) ? read_pos_end : write_pos;
            md5.update(buffer, 0, len);
            return md5.digest();
        }
    }
