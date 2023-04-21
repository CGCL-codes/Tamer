    private long[] getSpace(long[] min, long[] max) {
        long[] space = new long[min.length];
        if (max[min.length - 1] - min[min.length - 1] == 0) {
            space[min.length - 1] = 1;
        } else {
            space[min.length - 1] = max[min.length - 1] - min[min.length - 1];
        }
        for (int i = min.length - 2; i >= 0; i--) {
            if (max[i] - min[i] == 0) {
                space[i] = space[i + 1];
            } else {
                space[i] = 1;
                for (int k = i + 1; k < min.length; k++) {
                    if (space[i] <= Long.MAX_VALUE - space[k] * (max[k] - min[k])) {
                        space[i] += space[k] * (max[k] - min[k]);
                    } else {
                        space[i] = Long.MAX_VALUE;
                    }
                }
            }
        }
        return space;
    }
