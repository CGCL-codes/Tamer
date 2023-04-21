    public int getSquareNum(int number, int min, int max) {
        if ((number > 0) && (number <= powOf2[29])) {
            int avg = (min + max) / 2;
            if (powOf2[avg] == number) {
                return avg;
            } else {
                if (powOf2[avg] > number) {
                    return getSquareNum(number, min, avg - 1);
                }
                if (powOf2[avg] < number) {
                    return getSquareNum(number, avg + 1, max);
                }
            }
        }
        return -1;
    }
