    public BigInteger[] generateParameters() {
        int i, j, counter;
        byte[] u1, u2, v;
        byte[] seedBytes = new byte[m / 8];
        BigInteger SEED, U, q, R, V, W, X, p, g;
        int m_ = (m + 159) / 160;
        int L_ = (L + 159) / 160;
        int N_ = (L + 1023) / 1024;
        algorithm: while (true) {
            step4: while (true) {
                nextRandomBytes(seedBytes);
                SEED = new BigInteger(1, seedBytes).setBit(m - 1).setBit(0);
                U = BigInteger.ZERO;
                for (i = 0; i < m_; i++) {
                    u1 = SEED.add(BigInteger.valueOf(i)).toByteArray();
                    u2 = SEED.add(BigInteger.valueOf(m_ + i)).toByteArray();
                    sha.update(u1, 0, u1.length);
                    u1 = sha.digest();
                    sha.update(u2, 0, u2.length);
                    u2 = sha.digest();
                    for (j = 0; j < u1.length; j++) u1[j] ^= u2[j];
                    U = U.add(new BigInteger(1, u1).multiply(TWO.pow(160 * i)));
                }
                q = U.setBit(m - 1).setBit(0);
                if (q.isProbablePrime(80)) break step4;
            }
            counter = 0;
            step9: while (true) {
                R = SEED.add(BigInteger.valueOf(2 * m_)).add(BigInteger.valueOf(L_ * counter));
                V = BigInteger.ZERO;
                for (i = 0; i < L_; i++) {
                    v = R.toByteArray();
                    sha.update(v, 0, v.length);
                    v = sha.digest();
                    V = V.add(new BigInteger(1, v).multiply(TWO.pow(160 * i)));
                }
                W = V.mod(TWO.pow(L));
                X = W.setBit(L - 1);
                p = X.add(BigInteger.ONE).subtract(X.mod(TWO.multiply(q)));
                if (p.isProbablePrime(80)) {
                    break algorithm;
                }
                counter++;
                if (counter >= 4096 * N_) continue algorithm;
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
