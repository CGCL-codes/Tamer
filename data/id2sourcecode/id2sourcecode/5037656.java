    public static TuringMachine generateTestTuringMachine(char startChar, Random random, int maxCountArray, int maxCountTransition, boolean finiteAutomate, String alphabetIT, String alphabetWT, int maxLengthIT, int maxLengthWT) {
        int lengthIT = random.nextInt(maxLengthIT);
        int lengthWT = random.nextInt(maxLengthWT);
        int ItHead = random.nextInt(lengthIT + 2);
        int WtHead = random.nextInt(lengthWT + 1);
        return generateTestTuringMachine(startChar, new Random(), maxCountArray, maxCountTransition, finiteAutomate, alphabetIT, alphabetWT, lengthIT, lengthWT, ItHead, WtHead);
    }
