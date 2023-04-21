    public void init(byte[] keyMaterial) throws IllegalStateException {
        truncatedSize = macSize;
        if (truncatedSize < (macSize / 2)) throw new IllegalArgumentException("Truncated size too small"); else if (truncatedSize < 10) throw new IllegalArgumentException("Truncated size less than 80 bits");
        byte[] K = keyMaterial;
        if (K == null) {
            if (ipadHash == null) throw new IllegalArgumentException("Null key");
            underlyingHash = (IMessageDigest) ipadHash.clone();
            return;
        }
        if (K.length > blockSize) {
            underlyingHash.update(K, 0, K.length);
            K = underlyingHash.digest();
        }
        if (K.length < blockSize) {
            int limit = (K.length > blockSize) ? blockSize : K.length;
            byte[] newK = new byte[blockSize];
            System.arraycopy(K, 0, newK, 0, limit);
            K = newK;
        }
        underlyingHash.reset();
        opadHash = (IMessageDigest) underlyingHash.clone();
        if (ipad == null) ipad = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) ipad[i] = (byte) (K[i] ^ IPAD_BYTE);
        for (int i = 0; i < blockSize; i++) opadHash.update((byte) (K[i] ^ OPAD_BYTE));
        underlyingHash.update(ipad, 0, blockSize);
        ipadHash = (IMessageDigest) underlyingHash.clone();
        K = null;
    }
