    public boolean remove(IInterval interval) {
        checkInterval(interval);
        int begin = interval.getLeft();
        int end = interval.getRight();
        boolean modified = false;
        if (begin <= left && right <= end) {
            count--;
            dispose(interval);
            modified = true;
        } else {
            int mid = (left + right) / 2;
            if (begin < mid) {
                modified |= lson.remove(interval);
            }
            if (mid < end) {
                modified |= rson.remove(interval);
            }
        }
        return modified;
    }
