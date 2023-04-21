    public static boolean isIn(int[] r, int x) {
        boolean gevonden = false;
        int a = 0;
        int b = r.length - 1;
        int m = (a + b) / 2;
        while (a != b) {
            m = (a + b) / 2;
            System.out.println("m: " + m);
            if (r[m] == x) {
                a = m;
                b = a;
                System.out.println("gelijk");
            } else if (r[m] > x) {
                b = m - 1;
            } else if (r[m] < x) {
                a = m + 1;
            }
        }
        gevonden = (r[a] == x);
        return gevonden;
    }
