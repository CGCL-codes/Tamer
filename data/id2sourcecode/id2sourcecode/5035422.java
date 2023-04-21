    public int dichotomy(long searchKey) {
        int lowerBound = 0;
        int upperBound = array.length - 1;
        int curIn;
        while (true) {
            curIn = (lowerBound + upperBound) / 2;
            if (array[curIn] == searchKey) {
                return curIn;
            } else if (lowerBound > upperBound) {
                return array.length;
            } else {
                if (array[curIn] < searchKey) {
                    lowerBound = curIn + 1;
                } else {
                    upperBound = curIn - 1;
                }
            }
        }
    }
