    int findInd(int min, int max, double dist) {
        int mid = (min + max) / 2;
        if (inverseArray[mid] < dist) {
            if (max - mid == 1) {
                return max;
            }
            return findInd(mid, max, dist);
        } else if (mid - min == 1) {
            return mid;
        } else {
            return findInd(min, mid, dist);
        }
    }
