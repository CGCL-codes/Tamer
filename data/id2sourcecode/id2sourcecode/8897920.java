    public void init(byte[] key) throws Exception {
        if (key.length > bsize) {
            byte[] tmp = new byte[bsize];
            System.arraycopy(key, 0, tmp, 0, bsize);
            key = tmp;
        }
        if (key.length > B) {
            md.update(key, 0, key.length);
            key = md.digest();
        }
        k_ipad = new byte[B];
        System.arraycopy(key, 0, k_ipad, 0, key.length);
        k_opad = new byte[B];
        System.arraycopy(key, 0, k_opad, 0, key.length);
        for (int i = 0; i < B; i++) {
            k_ipad[i] ^= (byte) 0x36;
            k_opad[i] ^= (byte) 0x5c;
        }
        md.update(k_ipad, 0, B);
    }
