    public static int getRandomNum(int startNum, int endNum) {
        int randomNum = 0;
        try {
            SecureRandom rnd = new SecureRandom();
            do {
                randomNum = rnd.nextInt(endNum + 1);
            } while (randomNum < startNum);
        } catch (Exception e) {
            Logger.getLogger(EgovStringUtil.class).debug(e);
        }
        return randomNum;
    }
