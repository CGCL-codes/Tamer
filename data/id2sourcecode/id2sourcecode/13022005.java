    public int getXindex(int x) {
        int start = 0;
        int end = xs.length - 1;
        int mid = -1;
        while (start <= end) {
            mid = (end + start) / 2;
            if ((mid > 0) && ((xs[mid - 1] & NEXT_FLAG) > 0)) {
                mid--;
            }
            if (x < (xs[mid] & NEXT_BITS)) end = mid - 1; else if ((xs[mid] & NEXT_FLAG) > 0) {
                if (x > xs[mid + 1]) start = mid + 2; else if (x <= xs[mid + 1]) return mid;
            } else if (x > xs[mid]) start = mid + 1; else return mid;
        }
        return (-start) - 1;
    }
