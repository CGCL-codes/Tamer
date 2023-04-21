    private void RSA_generate_key(int bits, long e_value, Callback callback, Object cb_arg, int certainty, Random random) {
        BigInteger r0 = null, r1 = null, r2 = null, r3 = null;
        int bitsp, bitsq, ok = -1, i;
        int localN = 0;
        bitsp = (bits + 1) / 2;
        bitsq = bits - bitsp;
        e = BigInteger.valueOf(e_value);
        for (; ; ) {
            p = new BigInteger(bitsp, certainty, random);
            r2 = p.subtract(BigInteger.valueOf(1));
            r1 = r2.gcd(e);
            if (r1.equals(BigInteger.valueOf(1))) break;
            if (callback != null) callback.callback(2, localN++, cb_arg);
            p = null;
        }
        if (callback != null) callback.callback(3, 0, cb_arg);
        for (; ; ) {
            q = new BigInteger(bitsq, certainty, random);
            r2 = q.subtract(BigInteger.valueOf(1));
            r1 = r2.gcd(e);
            if (r1.equals(BigInteger.valueOf(1)) && p.compareTo(q) != 0) break;
            if (callback != null) callback.callback(2, localN++, cb_arg);
            q = null;
        }
        if (callback != null) callback.callback(3, 1, cb_arg);
        if (p.compareTo(q) < 0) {
            BigInteger tmp = p;
            p = q;
            q = tmp;
        }
        n = p.multiply(q);
        r1 = p.subtract(BigInteger.valueOf(1));
        r2 = q.subtract(BigInteger.valueOf(1));
        r0 = r1.multiply(r2);
        d = e.modInverse(r0);
        dmp1 = d.mod(r1);
        dmq1 = d.mod(r2);
        iqmp = q.modInverse(p);
    }
