    public static final void seedRandom() {
        try {
            URL url = new URL(HOTBITS_URL);
            DataInputStream din = new DataInputStream(url.openStream());
            m_Random.setSeed(din.readLong());
            din.close();
        } catch (Exception ex) {
            m_Random.setSeed(System.currentTimeMillis());
        }
    }
