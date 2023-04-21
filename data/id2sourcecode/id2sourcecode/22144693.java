    private int md5HashingAlg(byte[] key) {
        md5.reset();
        md5.update(key);
        byte[] bKey = md5.digest();
        int res = ((int) (bKey[3] & 0xFF) << 24) | ((int) (bKey[2] & 0xFF) << 16) | ((int) (bKey[1] & 0xFF) << 8) | (int) (bKey[0] & 0xFF);
        return res;
    }
