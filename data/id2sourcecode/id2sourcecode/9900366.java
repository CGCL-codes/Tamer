    private int getNumberBetween(int low, int high) {
        Random rand = new Random();
        int retInt = low - 1;
        while (retInt < low) retInt = rand.nextInt(high + 1);
        return retInt;
    }
