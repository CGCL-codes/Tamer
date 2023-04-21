    public void keyGenerate() {
        int pbitlength = (nbits + 1) / 2;
        int qbitlength = (nbits - pbitlength);
        PrimeTest pt = new MillerRabinPrimeTest();
        BaseCalculator bc = new BaseCalculator();
        for (; ; ) {
            p = bc.getBigRandomInt(pbitlength);
            if (!pt.primeTest(p, 15)) {
                continue;
            }
            break;
        }
        for (; ; ) {
            q = bc.getBigRandomInt(qbitlength);
            if (q.equals(p)) {
                continue;
            }
            if (!pt.primeTest(q, 15)) {
                continue;
            }
            break;
        }
        n = p.multiply(q);
        for (; ; ) {
            b = bc.getBigRandomInt(pbitlength);
            if (!b.gcd(n).equals(BigInteger.ONE)) {
                continue;
            }
            break;
        }
    }
