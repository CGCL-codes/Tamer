    public double remove(int index) {
        if (index >= length || index < 0) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        int i = index;
        double ret = values[i];
        while (i < length - 1) {
            values[i] = values[i + 1];
            i++;
        }
        length--;
        findMean();
        findMedian();
        findMode();
        findDeviation();
        return ret;
    }
