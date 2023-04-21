    private int insertIndex(Cursor cursor, int left, int right) {
        if (right <= left) {
            return left;
        }
        int pivot = (left + right) / 2;
        moveToPosition(pivot);
        int cres = comparator.compareCurrentRows(this, cursor);
        if (cres < 0) {
            return insertIndex(cursor, pivot + 1, right);
        } else if (cres > 0) {
            return insertIndex(cursor, left, pivot);
        }
        if (unique) {
            return -1;
        } else {
            return pivot;
        }
    }
