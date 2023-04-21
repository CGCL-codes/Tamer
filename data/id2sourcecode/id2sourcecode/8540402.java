    private final void init(byte[] key) {
        inner.reset();
        outer.reset();
        int keyLen = key.length;
        if (keyLen > blockSize) {
            inner.update(key);
            key = inner.digest();
            inner.reset();
            keyLen = blockSize;
        }
        System.arraycopy(key, 0, k_ipad, 0, keyLen);
        System.arraycopy(key, 0, k_opad, 0, keyLen);
        for (int i = 0; i < blockSize; i++) {
            k_ipad[i] ^= (byte) 0x36;
            k_opad[i] ^= (byte) 0x5c;
        }
        updatePads();
        if (inner instanceof Cloneable) {
            try {
                innerClone = (MessageDigest) inner.clone();
                outerClone = (MessageDigest) outer.clone();
            } catch (CloneNotSupportedException e) {
                innerClone = null;
                outerClone = null;
            }
        }
    }
