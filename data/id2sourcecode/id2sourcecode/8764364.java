    private static int greatestFactor(int a) {
        if (a <= 3) return a;
        while ((a & 1) == 0) {
            a >>= 1;
            if (a < 2 * 2) return a;
        }
        while (a % 3 == 0) {
            a /= 3;
            if (a < 3 * 3) return a;
        }
        int x = 5;
        int s = 5 * 5;
        while (a >= s && x <= 46337) {
            while (a % x == 0) {
                a /= x;
                if (a < s) return a;
            }
            x += 2;
            s = x * x;
            while (a % x == 0) {
                a /= x;
                if (a < s) return a;
            }
            x += 4;
            s = x * x;
        }
        return a;
    }
