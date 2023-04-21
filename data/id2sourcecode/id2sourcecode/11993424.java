    private void qSortTranslation(int lower, int upper) {
        if (lower >= upper) return;
        if (lower + 1 == upper) {
            if (compareTranslation(lower, upper) > 0) {
                swapTranslation(lower, upper);
            }
            return;
        }
        int pivot = lower + (upper - lower) / 2;
        int l = lower, u = upper;
        while (true) {
            while ((l < pivot) && (compareTranslation(l, pivot) <= 0)) {
                l++;
            }
            while ((u > pivot) && (compareTranslation(pivot, u) < 0)) {
                u--;
            }
            if (l >= u) {
                break;
            }
            if ((l < pivot) && (u > pivot)) {
                swapTranslation(l, u);
                l++;
                u--;
                continue;
            }
            if (l < pivot) {
                swapTranslation(l, pivot);
                pivot = l;
            } else {
                swapTranslation(pivot, u);
                pivot = u;
            }
        }
        qSortTranslation(lower, pivot - 1);
        qSortTranslation(pivot + 1, upper);
    }
