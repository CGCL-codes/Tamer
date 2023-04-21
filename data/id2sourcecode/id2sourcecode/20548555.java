    public synchronized void engineSetSeed(byte[] seed) {
        if (state != null) {
            digest.update(state);
            for (int i = 0; i < state.length; i++) state[i] = 0;
        }
        state = digest.digest(seed);
    }
