    protected static int[] makeEncodeTable(int seed) {
        int[] table = new int[256];
        int a = 6 + (seed % 250);
        int b = seed / 250;
        for (int i = 0; i < 256; i++) table[i] = i;
        for (int i = 1; i < 10001; i++) {
            int j = 1 + ((i * a + b) % 254);
            int t = table[j];
            table[j] = table[j + 1];
            table[j + 1] = t;
        }
        return table;
    }
