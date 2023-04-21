    static void store(Vector keyList, Vector valueList, long key, Object value) {
        int lowerBound = 0;
        int upperBound = keyList.size();
        while (lowerBound != upperBound) {
            int index = lowerBound + (upperBound - lowerBound) / 2;
            long indexKey = ((Long) keyList.elementAt(index)).longValue();
            if (indexKey > key) {
                if (index == upperBound) {
                    upperBound--;
                } else {
                    upperBound = index;
                }
            } else {
                if (index == lowerBound) {
                    lowerBound++;
                } else {
                    lowerBound = index;
                }
            }
        }
        keyList.insertElementAt(new Long(key), lowerBound);
        valueList.insertElementAt(value, lowerBound);
    }
