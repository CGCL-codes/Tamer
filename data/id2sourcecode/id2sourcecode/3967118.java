    private static final byte[] doCompute(byte[] ra, byte[] p_e, byte[] s, boolean encrypt) {
        assert ra != null && ra.length == 16;
        assert p_e != null;
        MessageDigest md5 = getMd5();
        byte[] b, c;
        int len = p_e.length;
        int n = len / 16;
        int left = len % 16;
        int count = (len + 16 - 1) / 16;
        byte[] cc = new byte[count * 16];
        for (int i = 0; i < n; i++) {
            md5.reset();
            md5.update(s);
            if (i == 0) {
                md5.update(ra);
            } else {
                if (encrypt) {
                    md5.update(cc, (i - 1) * 16, 16);
                } else {
                    md5.update(p_e, (i - 1) * 16, 16);
                }
            }
            c = md5.digest();
            for (int j = 0; j < 16; j++) {
                cc[i * 16 + j] = (byte) (p_e[i * 16 + j] ^ c[j]);
            }
        }
        if (n < count) {
            byte[] b16 = new byte[16];
            System.arraycopy(p_e, n * 16, b16, 0, left);
            md5.reset();
            md5.update(s);
            if (n == 0) {
                md5.update(ra);
            } else {
                if (encrypt) {
                    md5.update(cc, (n - 1) * 16, 16);
                } else {
                    md5.update(p_e, (n - 1) * 16, 16);
                }
            }
            c = md5.digest();
            for (int j = 0; j < 16; j++) {
                cc[n * 16 + j] = (byte) (b16[j] ^ c[j]);
            }
        }
        return cc;
    }
