    public long endReturnSUID() throws java.io.IOException {
        long h = 0;
        stream.flush();
        byte[] hash = md.digest();
        for (int i = 0; i < Math.min(hash.length, 8); i++) {
            int sh = i * 8;
            h += (long) (hash[i] & 255) << sh;
        }
        return h;
    }
