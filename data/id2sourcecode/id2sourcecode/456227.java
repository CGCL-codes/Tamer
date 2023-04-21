    public static int[] primes(int width) {
        int[] primes = new int[width / 2];
        int iPrime = 0;
        int work = width;
        while (work % 2 == 0) {
            primes[iPrime++] = 2;
            work /= 2;
        }
        int prime = 3;
        while (work > 1) {
            if (work % prime == 0) {
                primes[iPrime++] = prime;
                work /= prime;
            } else {
                prime += 2;
            }
        }
        int[] res = new int[iPrime];
        System.arraycopy(primes, 0, res, 0, iPrime);
        return res;
    }
