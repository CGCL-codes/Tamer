    private int zpevd(int a, int q, int l, int n) {
        int x = 1;
        for (int y = 0; y != l; y++) x = x * q;
        int z = 1;
        a = a % n;
        if (x == 0) return z;
        while (x != 0) {
            if (x % 2 == 0) {
                a = (a * a) % n;
                x = x / 2;
            } else {
                x = x - 1;
                z = (z * a) % n;
            }
        }
        return z;
    }
