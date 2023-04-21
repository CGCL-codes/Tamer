    private static void rotate(byte[] key) {
        byte[] x = new byte[64];
        System.arraycopy(key, 0, x, 0, x.length);
        for (int i = 0; i < 55; i++) {
            x[i] = x[i + 1];
        }
        x[27] = key[0];
        x[55] = key[28];
        System.arraycopy(x, 0, key, 0, key.length);
    }
