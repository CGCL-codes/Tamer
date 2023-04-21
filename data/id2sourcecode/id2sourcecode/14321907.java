    public static int rankOfDivisor(int input, int divisor) {
        int rank = 0;
        while (input % divisor == 0) {
            input /= divisor;
            rank++;
        }
        return rank;
    }
