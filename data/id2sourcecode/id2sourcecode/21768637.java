    public static <T> int search(Accessor<T> accessor, T value) {
        int min = accessor.start();
        int max = accessor.end() - 1;
        if (max < min) return -1;
        while (max > min) {
            int mid = min + (max - min) / 2;
            if (accessor.compare(value, mid) <= 0) max = mid; else min = mid + 1;
        }
        int rslt = accessor.compare(value, min);
        return (rslt == 0) ? min : (rslt < 0) ? -min - 1 : -(min + 1) - 1;
    }
