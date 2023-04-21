    private void init(byte[] key) {
        int i;
        if (key.length > PADLEN) {
            key = digest.digest(key);
            digest.reset();
        }
        ipad = new byte[PADLEN];
        opad = new byte[PADLEN];
        for (i = 0; i < key.length; i++) {
            ipad[i] = (byte) (key[i] ^ IPAD);
            opad[i] = (byte) (key[i] ^ OPAD);
        }
        for (; i < PADLEN; i++) {
            ipad[i] = IPAD;
            opad[i] = OPAD;
        }
        digest.update(ipad);
    }
