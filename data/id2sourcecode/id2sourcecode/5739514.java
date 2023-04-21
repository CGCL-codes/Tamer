    public static int largestWithoutGoingOver(int[] vals, int val) {
        int start = 0;
        int end = vals.length;
        if (vals.length == 0) return -1;
        if (val < vals[start]) return -1;
        if (val >= vals[end - 1]) return end - 1;
        while (start + 1 < end) {
            int mid = (start + end) / 2;
            if (val < vals[mid]) end = mid; else if (val > vals[mid]) start = mid; else return mid;
        }
        return start;
    }
