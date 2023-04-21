    public int pointToSymbol(int midCount) {
        int low = 0;
        int high = TOTAL_INDEX;
        while (true) {
            int mid = (high + low) / 2;
            if (_count[mid] > midCount) {
                if (high == mid) --high; else high = mid;
            } else if (_count[mid + 1] > midCount) {
                return (mid == EOF_INDEX) ? EOF : mid;
            } else {
                if (low == mid) ++low; else low = mid;
            }
        }
    }
