    public int random(int min, int max, int sd) {
        int mean = min + (max - min) / 2;
        int rand;
        do {
            rand = (int) (methods.random.nextGaussian() * sd + mean);
        } while (rand < min || rand >= max);
        return rand;
    }
