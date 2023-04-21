    public static boolean method378(byte abyte0[][], byte byte0, byte byte1) {
        int i = 0;
        if (abyte0[i][0] == byte0 && abyte0[i][1] == byte1) return true;
        int j = abyte0.length - 1;
        if (abyte0[j][0] == byte0 && abyte0[j][1] == byte1) return true;
        do {
            int k = (i + j) / 2;
            if (abyte0[k][0] == byte0 && abyte0[k][1] == byte1) return true;
            if (byte0 < abyte0[k][0] || byte0 == abyte0[k][0] && byte1 < abyte0[k][1]) j = k; else i = k;
        } while (i != j && i + 1 != j);
        return false;
    }
