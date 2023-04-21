    private int getSegmentNumber(double x) {
        int left = 0;
        int right = mNumPoints - 1;
        while (left + 1 < right) {
            int middle = (left + right) / 2;
            if (mX[middle] <= x) left = middle; else right = middle;
        }
        return left;
    }
