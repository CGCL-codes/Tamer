    public static String getRandomStr(char startChr, char endChr) {
        int randomInt;
        String randomStr = null;
        int startInt = Integer.valueOf(startChr);
        int endInt = Integer.valueOf(endChr);
        if (startInt > endInt) {
            throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
        }
        try {
            SecureRandom rnd = new SecureRandom();
            do {
                randomInt = rnd.nextInt(endInt + 1);
            } while (randomInt < startInt);
            randomStr = (char) randomInt + "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return randomStr;
    }
