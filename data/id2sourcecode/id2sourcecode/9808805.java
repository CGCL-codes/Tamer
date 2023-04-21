    public static int getTimeInterval(double[] times, int start, int end, double t) {
        if (start < 0) throw new IllegalArgumentException("The starting index must not be negative");
        int n = end - start;
        if (n < 0) throw new IllegalArgumentException("The ending index must be greater than or equal to the starting index");
        if (t < times[start]) return -1;
        if (t >= times[end]) return n;
        int start0 = start;
        int mid = (start + end) / 2;
        while (t < times[mid] || t >= times[mid + 1]) {
            if (start == end) throw new IllegalStateException();
            if (t < times[mid]) end = mid - 1; else start = mid + 1;
            mid = (start + end) / 2;
        }
        return mid - start0;
    }
