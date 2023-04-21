    public static final byte[] HMAC(String text, String key, MessageDigest md5Digester) {
        final int textLen = (null == text) ? 0 : text.length(), keyLen = (null == key) ? 0 : key.length();
        if ((textLen <= 0) || (keyLen <= 0) || (null == md5Digester)) throw new IllegalArgumentException("Null/empty text/key/digester");
        final byte[] xorIPAD = new byte[MAX_MD5_PAD_LEN], xorOPAD = new byte[MAX_MD5_PAD_LEN];
        byte[] keyBytes = getAuthStringBytes(key);
        if (keyLen > MAX_MD5_PAD_LEN) keyBytes = md5Digester.digest(keyBytes);
        for (int kIndex = 0; kIndex < keyBytes.length; kIndex++) {
            xorIPAD[kIndex] = (byte) (keyBytes[kIndex] ^ 0x36);
            xorOPAD[kIndex] = (byte) (keyBytes[kIndex] ^ 0x5C);
        }
        for (int pIndex = keyBytes.length; pIndex < MAX_MD5_PAD_LEN; pIndex++) {
            xorIPAD[pIndex] = 0x36;
            xorOPAD[pIndex] = 0x5C;
        }
        {
            md5Digester.update(xorIPAD);
            final byte[] chlngBytes = new byte[textLen];
            for (int cIndex = 0; cIndex < textLen; cIndex++) {
                final char cch = text.charAt(cIndex);
                if ((cch < 0) || (cch > 255)) throw new IllegalStateException("Text char at position=" + cIndex + " is not in 0-255 range");
                chlngBytes[cIndex] = (byte) cch;
            }
            md5Digester.update(chlngBytes);
        }
        {
            final byte[] imedDigest = md5Digester.digest();
            md5Digester.update(xorOPAD);
            md5Digester.update(imedDigest);
            return md5Digester.digest();
        }
    }
