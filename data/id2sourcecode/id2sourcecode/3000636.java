    public static int generateRandomNumber(int maxSize) {
        Random rand = new Random();
        int randnum = rand.nextInt();
        randnum = rand.nextInt(maxSize + 1);
        return randnum;
    }
