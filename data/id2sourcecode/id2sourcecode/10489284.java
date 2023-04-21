    private int getRandomNumber(int lowerLimit, int upperLimit) {
        int randomNumber;
        while (true) {
            randomNumber = random.nextInt(upperLimit + 1);
            if (randomNumber >= lowerLimit) {
                break;
            }
        }
        return randomNumber;
    }
