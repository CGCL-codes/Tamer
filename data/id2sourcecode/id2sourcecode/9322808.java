    @Override
    public void addSeedMaterial(byte[] seed) {
        int pos = 0;
        for (int itr = 0; itr < seed.length; ++itr) {
            if (pos == 0) {
                for (int i = 0; i < 3; ++i) x[i] = x[i + 1];
                x[3] = seed[itr] & 0xff;
                pos = 1;
            } else {
                x[3] = (x[3] << 8) | (seed[itr] & 0xff);
                pos = (pos + 1 == 4 ? 0 : pos + 1);
            }
        }
    }
