    public boolean insert(IInterval interval) {
        checkInterval(interval);
        int begin = interval.getLeft();
        int end = interval.getRight();
        boolean modified = false;
        if (begin <= left && right <= end) {
            count++;
            update(interval);
            modified = true;
        } else {
            int mid = (left + right) / 2;
            if (begin < mid) {
                modified |= lson.insert(interval);
            }
            if (mid < end) {
                modified |= rson.insert(interval);
            }
        }
        return modified;
    }
