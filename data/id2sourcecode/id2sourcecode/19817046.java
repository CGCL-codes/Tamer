    @Override
    protected void memcpy(int destination, int source, int length, boolean checkOverlap) {
        for (int i = 0; i < length; i++) {
            write8(destination + i, (byte) mem.read8(source + i));
        }
    }
