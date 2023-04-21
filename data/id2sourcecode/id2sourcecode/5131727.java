    public void reorderTable(int[] t, int[] dest) {
        final int a = t[0], b = t[1], c = t[2], d = t[3], e = t[4], f = t[5];
        final int c1 = a + d, c2 = b + e, c3 = c + f;
        final int n1 = a + b + c, n2 = d + e + f;
        for (int i = 0; i < t.length; i++) {
            dest[i] = t[i];
        }
        int cmin = Math.min(c1, Math.min(c2, c3));
        if (c1 > cmin) {
            int col = c2 > cmin ? 2 : 1;
            dest[0] = t[col];
            dest[3] = t[col + 3];
            dest[col] = t[0];
            dest[col + 3] = t[3];
        }
        if (n1 > n2) {
            int temp;
            for (int i = 0; i < 3; i++) {
                temp = dest[i];
                dest[i] = dest[i + 3];
                dest[i + 3] = temp;
            }
        }
    }
