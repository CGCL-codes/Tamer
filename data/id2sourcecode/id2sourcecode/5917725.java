    private int procedure_A(int x0, int c, BigInteger[] pq, int size) {
        while (x0 < 0 || x0 > 65536) {
            x0 = init_random.nextInt() / 32768;
        }
        while ((c < 0 || c > 65536) || (c / 2 == 0)) {
            c = init_random.nextInt() / 32768 + 1;
        }
        BigInteger C = new BigInteger(Integer.toString(c));
        BigInteger constA16 = new BigInteger("19381");
        BigInteger[] y = new BigInteger[1];
        y[0] = new BigInteger(Integer.toString(x0));
        int[] t = new int[1];
        t[0] = size;
        int s = 0;
        for (int i = 0; t[i] >= 17; i++) {
            int tmp_t[] = new int[t.length + 1];
            System.arraycopy(t, 0, tmp_t, 0, t.length);
            t = new int[tmp_t.length];
            System.arraycopy(tmp_t, 0, t, 0, tmp_t.length);
            t[i + 1] = t[i] / 2;
            s = i + 1;
        }
        BigInteger p[] = new BigInteger[s + 1];
        p[s] = new BigInteger("8003", 16);
        int m = s - 1;
        for (int i = 0; i < s; i++) {
            int rm = t[m] / 16;
            step6: for (; ; ) {
                BigInteger tmp_y[] = new BigInteger[y.length];
                System.arraycopy(y, 0, tmp_y, 0, y.length);
                y = new BigInteger[rm + 1];
                System.arraycopy(tmp_y, 0, y, 0, tmp_y.length);
                for (int j = 0; j < rm; j++) {
                    y[j + 1] = (y[j].multiply(constA16).add(C)).mod(TWO.pow(16));
                }
                BigInteger Ym = new BigInteger("0");
                for (int j = 0; j < rm; j++) {
                    Ym = Ym.add(y[j].multiply(TWO.pow(16 * j)));
                }
                y[0] = y[rm];
                BigInteger N = TWO.pow(t[m] - 1).divide(p[m + 1]).add((TWO.pow(t[m] - 1).multiply(Ym)).divide(p[m + 1].multiply(TWO.pow(16 * rm))));
                if (N.mod(TWO).compareTo(ONE) == 0) {
                    N = N.add(ONE);
                }
                int k = 0;
                step11: for (; ; ) {
                    p[m] = p[m + 1].multiply(N.add(BigInteger.valueOf(k))).add(ONE);
                    if (p[m].compareTo(TWO.pow(t[m])) == 1) {
                        continue step6;
                    }
                    if ((TWO.modPow(p[m + 1].multiply(N.add(BigInteger.valueOf(k))), p[m]).compareTo(ONE) == 0) && (TWO.modPow(N.add(BigInteger.valueOf(k)), p[m]).compareTo(ONE) != 0)) {
                        m -= 1;
                        break;
                    } else {
                        k += 2;
                        continue step11;
                    }
                }
                if (m >= 0) {
                    break;
                } else {
                    pq[0] = p[0];
                    pq[1] = p[1];
                    return y[0].intValue();
                }
            }
        }
        return y[0].intValue();
    }
