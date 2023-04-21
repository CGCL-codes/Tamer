    public static int[] getFirstDifference(final int[] population) {
        Arrays.sort(population);
        final int[] jumps = new int[population.length - 1];
        for (int i = 0; i < jumps.length; i++) {
            jumps[i] = population[i + 1] - population[i];
        }
        return jumps;
    }
