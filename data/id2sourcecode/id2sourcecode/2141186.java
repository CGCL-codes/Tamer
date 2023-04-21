    public byte[] decrypt(byte[] in, int offset, int len, byte[] k) {
        if (k == null) return null;
        crypt = preCrypt = 0;
        this.key = k;
        int count;
        byte[] m = new byte[offset + 8];
        if ((len % 8 != 0) || (len < 16)) return null;
        prePlain = decipher(in, offset);
        pos = prePlain[0] & 0x7;
        count = len - pos - 10;
        if (count < 0) return null;
        for (int i = offset; i < m.length; i++) m[i] = 0;
        out = new byte[count];
        preCrypt = 0;
        crypt = 8;
        contextStart = 8;
        pos++;
        padding = 1;
        while (padding <= 2) {
            if (pos < 8) {
                pos++;
                padding++;
            }
            if (pos == 8) {
                m = in;
                if (!decrypt8Bytes(in, offset, len)) return null;
            }
        }
        int i = 0;
        while (count != 0) {
            if (pos < 8) {
                out[i] = (byte) (m[offset + preCrypt + pos] ^ prePlain[pos]);
                i++;
                count--;
                pos++;
            }
            if (pos == 8) {
                m = in;
                preCrypt = crypt - 8;
                if (!decrypt8Bytes(in, offset, len)) return null;
            }
        }
        for (padding = 1; padding < 8; padding++) {
            if (pos < 8) {
                if ((m[offset + preCrypt + pos] ^ prePlain[pos]) != 0) return null;
                pos++;
            }
            if (pos == 8) {
                m = in;
                preCrypt = crypt;
                if (!decrypt8Bytes(in, offset, len)) return null;
            }
        }
        return out;
    }
