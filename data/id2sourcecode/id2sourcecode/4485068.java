    protected int getIndexToInsert(Date date, int downLimit, int upLimit, int startPoint) {
        Date dateInList = list.get(startPoint).getDate();
        int compareTo = date.compareTo(dateInList);
        if (upLimit - downLimit < 3) {
            for (int i = upLimit; i >= downLimit; i--) {
                dateInList = list.get(i).getDate();
                compareTo = date.compareTo(dateInList);
                if (compareTo == 0) {
                    return i;
                } else if (compareTo > 0) {
                    return i + 1;
                }
            }
            return downLimit;
        }
        if (compareTo < 0) {
            upLimit = startPoint;
        } else if (compareTo > 0) {
            downLimit = startPoint;
        } else {
            return startPoint;
        }
        startPoint = (downLimit + upLimit) / 2;
        return getIndexToInsert(date, downLimit, upLimit, startPoint);
    }
