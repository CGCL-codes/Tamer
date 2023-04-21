    static byte[] derive(char[] chars, byte[] salt, int ic, int n, int type) {
        int length = chars.length * 2;
        if (length != 0) {
            length += 2;
        }
        byte[] passwd = new byte[length];
        for (int i = 0, j = 0; i < chars.length; i++, j += 2) {
            passwd[j] = (byte) ((chars[i] >>> 8) & 0xFF);
            passwd[j + 1] = (byte) (chars[i] & 0xFF);
        }
        int v = 512 / 8;
        int u = 160 / 8;
        int c = roundup(n, u) / u;
        byte[] D = new byte[v];
        int s = roundup(salt.length, v);
        int p = roundup(passwd.length, v);
        byte[] I = new byte[s + p];
        byte[] key = new byte[n];
        Arrays.fill(D, (byte) type);
        concat(salt, I, 0, s);
        concat(passwd, I, s, p);
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA1");
            byte[] Ai;
            byte[] B = new byte[v];
            byte[] tmp = new byte[v];
            int i = 0;
            for (; ; i++, n -= u) {
                sha.update(D);
                sha.update(I);
                Ai = sha.digest();
                for (int r = 1; r < ic; r++) Ai = sha.digest(Ai);
                System.arraycopy(Ai, 0, key, u * i, Math.min(n, u));
                if (i + 1 == c) break;
                concat(Ai, B, 0, B.length);
                BigInteger B1;
                B1 = new BigInteger(1, B).add(BigInteger.ONE);
                for (int j = 0; j < I.length; j += v) {
                    BigInteger Ij;
                    int trunc;
                    if (tmp.length != v) tmp = new byte[v];
                    System.arraycopy(I, j, tmp, 0, v);
                    Ij = new BigInteger(1, tmp);
                    Ij = Ij.add(B1);
                    tmp = Ij.toByteArray();
                    trunc = tmp.length - v;
                    if (trunc >= 0) {
                        System.arraycopy(tmp, trunc, I, j, v);
                    } else if (trunc < 0) {
                        Arrays.fill(I, j, j + (-trunc), (byte) 0);
                        System.arraycopy(tmp, 0, I, j + (-trunc), tmp.length);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("internal error: " + e);
        }
        return key;
    }
