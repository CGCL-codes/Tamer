    public byte[] encode(byte[] mHash, int emBits, byte[] salt) {
        int sLen = salt.length;
        if (hLen != mHash.length) throw new IllegalArgumentException("wrong hash");
        if (emBits < (8 * hLen + 8 * sLen + 9)) throw new IllegalArgumentException("encoding error");
        int emLen = (emBits + 7) / 8;
        byte[] H;
        int i;
        synchronized (hash) {
            for (i = 0; i < 8; i++) hash.update((byte) 0x00);
            hash.update(mHash, 0, hLen);
            hash.update(salt, 0, sLen);
            H = hash.digest();
        }
        byte[] DB = new byte[emLen - sLen - hLen - 2 + 1 + sLen];
        DB[emLen - sLen - hLen - 2] = 0x01;
        System.arraycopy(salt, 0, DB, emLen - sLen - hLen - 1, sLen);
        byte[] dbMask = MGF(H, emLen - hLen - 1);
        if (Configuration.DEBUG) {
            log.fine("dbMask (encode): " + Util.toString(dbMask));
            log.fine("DB (encode): " + Util.toString(DB));
        }
        for (i = 0; i < DB.length; i++) DB[i] = (byte) (DB[i] ^ dbMask[i]);
        DB[0] &= (0xFF >>> (8 * emLen - emBits));
        byte[] result = new byte[emLen];
        System.arraycopy(DB, 0, result, 0, emLen - hLen - 1);
        System.arraycopy(H, 0, result, emLen - hLen - 1, hLen);
        result[emLen - 1] = (byte) 0xBC;
        return result;
    }
