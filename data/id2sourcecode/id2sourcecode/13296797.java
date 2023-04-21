    public void mergeSortImage(int lower, int upper) {
        if (lower >= upper) {
            return;
        }
        int mid = (lower + upper) / 2;
        mergeSortImage(lower, mid);
        mergeSortImage(mid + 1, upper);
        merge(lower, mid, mid + 1, upper);
    }
