    public void keyGenerate() {
        Random rand = new Random(System.currentTimeMillis());
        e = new BigInteger(20, 15, rand);
        int pbitlength = (nbits + 1) / 2;
        int qbitlength = (nbits - pbitlength);
        PrimeTest pt = new MillerRabinPrimeTest();
        BaseCalculator bc = new BaseCalculator();
        for (; ; ) {
            p = bc.getBigRandomInt(pbitlength);
            if (p.mod(e).equals(BigInteger.ONE)) {
                continue;
            }
            if (!pt.primeTest(p, 15)) {
                continue;
            }
            if (e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                break;
            }
        }
        for (; ; ) {
            q = bc.getBigRandomInt(qbitlength);
            if (q.equals(p)) {
                continue;
            }
            if (q.mod(e).equals(BigInteger.ONE)) {
                continue;
            }
            if (!pt.primeTest(q, 15)) {
                continue;
            }
            if (e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                break;
            }
        }
        n = p.multiply(q);
        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
    }
