    private void mergingCount(int start, int end) {
        if (end <= start) return;
        if (end == start + 1) {
            if (array[end] < array[start]) {
                count++;
                swap(start, end);
            }
            return;
        }
        int m = (start + end) / 2;
        mergingCount(start, m);
        mergingCount(m + 1, end);
        merge(start, end);
    }
