    public BigInteger[] generateParameters() {
        int counter, offset;
        BigInteger SEED, alpha, U, q, OFFSET, SEED_PLUS_OFFSET, W, X, p, c, g;
        byte[] a, u;
        byte[] kb = new byte[20];
        int b = (L - 1) % 160;
        int n = (L - 1 - b) / 160;
        BigInteger[] V = new BigInteger[n + 1];
        algorithm: while (true) {
            step1: while (true) {
                nextRandomBytes(kb);
                SEED = new BigInteger(1, kb).setBit(159).setBit(0);
                alpha = SEED.add(BigInteger.ONE).mod(TWO_POW_160);
                synchronized (sha) {
                    a = SEED.toByteArray();
                    sha.update(a, 0, a.length);
                    a = sha.digest();
                    u = alpha.toByteArray();
                    sha.update(u, 0, u.length);
                    u = sha.digest();
                }
                for (int i = 0; i < a.length; i++) a[i] ^= u[i];
                U = new BigInteger(1, a);
                q = U.setBit(159).setBit(0);
                if (q.isProbablePrime(80)) break step1;
            }
            counter = 0;
            offset = 2;
            step7: while (true) {
                OFFSET = BigInteger.valueOf(offset & 0xFFFFFFFFL);
                SEED_PLUS_OFFSET = SEED.add(OFFSET);
                synchronized (sha) {
                    for (int k = 0; k <= n; k++) {
                        a = SEED_PLUS_OFFSET.add(BigInteger.valueOf(k & 0xFFFFFFFFL)).mod(TWO_POW_160).toByteArray();
                        sha.update(a, 0, a.length);
                        V[k] = new BigInteger(1, sha.digest());
                    }
                }
                W = V[0];
                for (int k = 1; k < n; k++) W = W.add(V[k].multiply(TWO.pow(k * 160)));
                W = W.add(V[n].mod(TWO.pow(b)).multiply(TWO.pow(n * 160)));
                X = W.add(TWO.pow(L - 1));
                c = X.mod(TWO.multiply(q));
                p = X.subtract(c.subtract(BigInteger.ONE));
                if (p.compareTo(TWO.pow(L - 1)) >= 0) {
                    if (p.isProbablePrime(80)) break algorithm;
                }
                counter++;
                offset += n + 1;
                if (counter >= 4096) continue algorithm;
            }
        }
        BigInteger e = p.subtract(BigInteger.ONE).divide(q);
        BigInteger h = TWO;
        BigInteger p_minus_1 = p.subtract(BigInteger.ONE);
        g = TWO;
        for (; h.compareTo(p_minus_1) < 0; h = h.add(BigInteger.ONE)) {
            g = h.modPow(e, p);
            if (!g.equals(BigInteger.ONE)) break;
        }
        return new BigInteger[] { SEED, BigInteger.valueOf(counter), q, p, e, g };
    }
