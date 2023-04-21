    public int getYindex(long y) {
        int start = 0;
        int end = ys.length - 1;
        int mid = -1;
        while (start <= end) {
            mid = (end + start) / 2;
            if ((mid > 0) && ((ys[mid - 1] & NEXT_FLAGL) > 0)) {
                mid--;
            }
            if (y < (ys[mid] & NEXT_BITSL)) end = mid - 1; else if ((ys[mid] & NEXT_FLAGL) > 0) {
                if (y > ys[mid + 1]) start = mid + 2; else if (y <= ys[mid + 1]) return mid;
            } else if (y > ys[mid]) start = mid + 1; else return mid;
        }
        return (-start) - 1;
    }
