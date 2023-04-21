    public static int getRndInt(int beginRange, int endRange) {
        int result = 0;
        if (endRange > beginRange && beginRange > 0) {
            result = beginRange + _rnd.nextInt(endRange - beginRange);
        }
        return result;
    }
