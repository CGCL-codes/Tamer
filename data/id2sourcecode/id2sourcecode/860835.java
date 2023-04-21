    static long iterate(int n, int Perm[], int Perm1[], int Zaehl[], int PermMax[]) {
        long BishMax = -1, Spiegelungsanzahl;
        int r, i, k;
        for (i = 0; i < n; i++) Perm1[i] = i;
        r = n;
        while (true) {
            while (r != 1) {
                Zaehl[r - 1] = r;
                r = r - 1;
            }
            if (Perm1[0] != 0) {
                i = n - 1;
                if (Perm1[i] != i) {
                    for (i = 0; i < n; i = i + 1) Perm[i] = Perm1[i];
                    Spiegelungsanzahl = 0;
                    while (!((k = Perm[0]) == 0)) {
                        int k2 = (k + 1) >> 1;
                        for (i = 0; i < k2; i = i + 1) {
                            int temp = Perm[i];
                            Perm[i] = Perm[k - i];
                            Perm[k - i] = temp;
                        }
                        Spiegelungsanzahl = Spiegelungsanzahl + 1;
                    }
                    if (Spiegelungsanzahl > BishMax) {
                        BishMax = Spiegelungsanzahl;
                        for (i = 0; i < n; i = i + 1) PermMax[i] = Perm1[i];
                    }
                }
            }
            while (true) {
                if (r == n) return (BishMax);
                {
                    int Perm0;
                    Perm0 = Perm1[0];
                    i = 0;
                    while (i < r) {
                        k = i + 1;
                        Perm1[i] = Perm1[k];
                        i = k;
                    }
                    Perm1[r] = Perm0;
                }
                if ((Zaehl[r] = Zaehl[r] - 1) > 0) break;
                r = r + 1;
            }
        }
    }
