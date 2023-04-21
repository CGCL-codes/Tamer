    public static final int getNearestIndex(float mass, float[] masses, int lowIndex, int highIndex) {
        float lm = masses[lowIndex];
        float hm = masses[highIndex];
        float m = (lm + hm) / 2;
        if (mass > m) return highIndex;
        return lowIndex;
    }
