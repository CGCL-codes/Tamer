    private void mixRandomPool(byte[] buf) {
        int hashSize = hash.hashSize();
        for (int i = 0; i < buf.length; i += hashSize) {
            if (i == 0) hash.update(buf, buf.length - hashSize, hashSize); else hash.update(buf, i - hashSize, hashSize);
            if (i + 64 < buf.length) hash.update(buf, i, 64); else {
                hash.update(buf, i, buf.length - i);
                hash.update(buf, 0, 64 - (buf.length - i));
            }
            byte[] digest = hash.digest();
            System.arraycopy(digest, 0, buf, i, hashSize);
        }
    }
