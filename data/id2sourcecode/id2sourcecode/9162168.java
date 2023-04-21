    public boolean contains(int value) {
        if (value > endPoints[used - 1]) {
            return false;
        }
        if (value < startPoints[0]) {
            return false;
        }
        int i = 0;
        int j = used;
        do {
            int mid = i + (j - i) / 2;
            if (endPoints[mid] < value) {
                i = Math.max(mid, i + 1);
            } else if (startPoints[mid] > value) {
                j = Math.min(mid, j - 1);
            } else {
                return true;
            }
        } while (i != j);
        return false;
    }
