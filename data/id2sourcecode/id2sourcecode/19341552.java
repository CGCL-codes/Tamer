    public static MinMaxResult minmaxs(final int[] items, final int num) {
        if (num < 3) {
            return (MinMaxResult.NOT_ENOUGH_INPUT_ITEMS);
        }
        final int minmax_alloc = num - 2;
        final int[] minmax_val = new int[minmax_alloc];
        MinMaxType[] minmax_type = new MinMaxType[minmax_alloc];
        int[] minmax_i = new int[minmax_alloc];
        int minmax_num = 0;
        int i = 0;
        int diff = items[1] - items[0];
        int state;
        if (diff > 0) state = 1; else if (diff < 0) state = -1; else state = 0;
        int start = 0;
        i++;
        while (i < num - 1) {
            diff = items[i + 1] - items[i];
            if (diff > 0) {
                if (state == 1) {
                    start = i;
                } else if (state == -1) {
                    int loc = (start + i) / 2;
                    minmax_val[minmax_num] = items[loc];
                    minmax_type[minmax_num] = MinMaxType.MINIMA;
                    minmax_i[minmax_num++] = loc;
                    state = 1;
                    start = i;
                } else {
                    if (i - start > 1) {
                        int loc = (start + i) / 2;
                        minmax_val[minmax_num] = items[loc];
                        minmax_type[minmax_num] = MinMaxType.MINIMA;
                        minmax_i[minmax_num++] = loc;
                        state = 1;
                        start = i;
                    } else {
                        state = 1;
                        start = i;
                    }
                }
            } else if (diff < 0) {
                if (state == -1) {
                    start = i;
                } else if (state == 1) {
                    int loc = (start + i) / 2;
                    minmax_val[minmax_num] = items[loc];
                    minmax_type[minmax_num] = MinMaxType.MAXIMA;
                    minmax_i[minmax_num++] = loc;
                    state = -1;
                    start = i;
                } else {
                    if (i - start > 1) {
                        int loc = (start + i) / 2;
                        minmax_val[minmax_num] = items[loc];
                        minmax_type[minmax_num] = MinMaxType.MAXIMA;
                        minmax_i[minmax_num++] = loc;
                        state = -1;
                        start = i;
                    } else {
                        state = -1;
                        start = i;
                    }
                }
            }
            i++;
        }
        return new MinMaxResult(minmax_alloc, minmax_num, minmax_i, minmax_type, minmax_val);
    }
