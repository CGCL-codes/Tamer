    private static int YearFromTime(double t) {
        int lo = (int) java.lang.Math.floor((t / msPerDay) / 366) + 1970;
        int hi = (int) java.lang.Math.floor((t / msPerDay) / 365) + 1970;
        int mid;
        if (hi < lo) {
            int temp = lo;
            lo = hi;
            hi = temp;
        }
        while (hi > lo) {
            mid = (hi + lo) / 2;
            if (TimeFromYear(mid) > t) {
                hi = mid - 1;
            } else {
                if (TimeFromYear(mid) <= t) {
                    int temp = mid + 1;
                    if (TimeFromYear(temp) > t) {
                        return mid;
                    }
                    lo = mid + 1;
                }
            }
        }
        return lo;
    }
