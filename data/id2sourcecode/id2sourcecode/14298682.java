    private int findFieldKey(int key) {
        int lowerBound = 0;
        int upperBound = fieldKeys.length;
        while (lowerBound != upperBound) {
            int index = lowerBound + (upperBound - lowerBound) / 2;
            int indexKey = fieldKeys[index];
            if (indexKey > key) {
                if (index == upperBound) {
                    upperBound--;
                } else {
                    upperBound = index;
                }
            } else if (indexKey == key) {
                return index;
            } else {
                if (index == lowerBound) {
                    lowerBound++;
                } else {
                    lowerBound = index;
                }
            }
        }
        return ~lowerBound;
    }
