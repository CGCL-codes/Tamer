    private int insertPos(Object value) {
        if (size() == 0) return 0;
        int begin = 0;
        int end = size();
        for (; ; ) {
            int mid = begin + (end - begin) / 2;
            Object midObj = array[mid];
            int cmp = compare(value, midObj);
            if (cmp == 0) return mid; else if (cmp < 0) {
                if (mid == begin) return begin;
                end = mid;
            } else {
                if (mid == end - 1) return end;
                begin = mid;
            }
        }
    }
