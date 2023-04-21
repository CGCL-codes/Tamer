    protected byte[] getDigestMD5withoutLast() {
        synchronized (md5) {
            md5.update(buffer, 0, marked_pos);
            return md5.digest();
        }
    }
