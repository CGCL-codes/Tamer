    protected static int getRandomValue(int min, int max) {
        Random rand = new Random();
        int v = min + rand.nextInt(max - min);
        return rand.nextBoolean() ? v : -v;
    }
