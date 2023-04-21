    protected int getRandomValue(int min, int max) {
        int v = min + rand.nextInt(max - min);
        return rand.nextBoolean() ? v : -v;
    }
