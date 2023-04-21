    public boolean decode(byte[] mHash, byte[] EM, int emBits, int sLen) {
        if (Configuration.DEBUG) {
            log.fine("mHash: " + Util.toString(mHash));
            log.fine("EM: " + Util.toString(EM));
            log.fine("emBits: " + String.valueOf(emBits));
            log.fine("sLen: " + String.valueOf(sLen));
        }
        if (sLen < 0) throw new IllegalArgumentException("sLen");
        if (hLen != mHash.length) {
            if (Configuration.DEBUG) log.fine("hLen != mHash.length; hLen: " + String.valueOf(hLen));
            throw new IllegalArgumentException("wrong hash");
        }
        if (emBits < (8 * hLen + 8 * sLen + 9)) {
            if (Configuration.DEBUG) log.fine("emBits < (8hLen + 8sLen + 9); sLen: " + String.valueOf(sLen));
            throw new IllegalArgumentException("decoding error");
        }
        int emLen = (emBits + 7) / 8;
        if ((EM[EM.length - 1] & 0xFF) != 0xBC) {
            if (Configuration.DEBUG) log.fine("EM does not end with 0xBC");
            return false;
        }
        if ((EM[0] & (0xFF << (8 - (8 * emLen - emBits)))) != 0) {
            if (Configuration.DEBUG) log.fine("Leftmost 8emLen - emBits bits of EM are not 0s");
            return false;
        }
        byte[] DB = new byte[emLen - hLen - 1];
        byte[] H = new byte[hLen];
        System.arraycopy(EM, 0, DB, 0, emLen - hLen - 1);
        System.arraycopy(EM, emLen - hLen - 1, H, 0, hLen);
        byte[] dbMask = MGF(H, emLen - hLen - 1);
        int i;
        for (i = 0; i < DB.length; i++) DB[i] = (byte) (DB[i] ^ dbMask[i]);
        DB[0] &= (0xFF >>> (8 * emLen - emBits));
        if (Configuration.DEBUG) {
            log.fine("dbMask (decode): " + Util.toString(dbMask));
            log.fine("DB (decode): " + Util.toString(DB));
        }
        for (i = 0; i < (emLen - hLen - sLen - 2); i++) {
            if (DB[i] != 0) {
                if (Configuration.DEBUG) log.fine("DB[" + String.valueOf(i) + "] != 0x00");
                return false;
            }
        }
        if (DB[i] != 0x01) {
            if (Configuration.DEBUG) log.fine("DB's byte at position (emLen -hLen -sLen -2); i.e. " + String.valueOf(i) + " is not 0x01");
            return false;
        }
        byte[] salt = new byte[sLen];
        System.arraycopy(DB, DB.length - sLen, salt, 0, sLen);
        byte[] H0;
        synchronized (hash) {
            for (i = 0; i < 8; i++) hash.update((byte) 0x00);
            hash.update(mHash, 0, hLen);
            hash.update(salt, 0, sLen);
            H0 = hash.digest();
        }
        return Arrays.equals(H, H0);
    }
