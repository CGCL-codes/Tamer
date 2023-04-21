    static int getNearestDipeptideIndex(float mass, int lowIndex, int highIndex) {
        float lm = dipeptide[lowIndex].mass;
        float hm = dipeptide[highIndex].mass;
        float m = (lm + hm) / 2;
        if (mass > m) return highIndex;
        return lowIndex;
    }
