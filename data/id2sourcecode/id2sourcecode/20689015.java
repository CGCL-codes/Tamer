    public static final void seedRandom() {
        if (c_SeedWithHotBits) {
            try {
                URL url = new URL(HOTBITS_URL);
                DataInputStream din = new DataInputStream(url.openStream());
                c_Random.setSeed(din.readLong());
                din.close();
            } catch (Exception ex) {
                c_Random.setSeed(c_SeedRandom.getSeed(8));
            }
        } else {
            c_Random.setSeed(c_SeedRandom.getSeed(8));
        }
    }
