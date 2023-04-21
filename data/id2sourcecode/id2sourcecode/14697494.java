    private void sortDataCategory(int start, int end) {
        Category pivot, temp;
        int lo = start;
        int hi = end;
        if (lo >= hi) return; else if (lo == hi - 1) {
            if (categoryArr[lo].getLabel().compareTo(categoryArr[hi].getLabel()) > 0) {
                pivot = categoryArr[lo];
                categoryArr[lo] = categoryArr[hi];
                categoryArr[hi] = pivot;
            }
            return;
        }
        tempInt = (lo + hi) / 2;
        pivot = categoryArr[tempInt];
        categoryArr[tempInt] = categoryArr[hi];
        categoryArr[hi] = pivot;
        while (lo < hi) {
            while ((categoryArr[lo].getLabel().compareTo(pivot.getLabel()) <= 0) && (lo < hi)) lo++;
            while ((pivot.getLabel().compareTo(categoryArr[hi].getLabel()) <= 0) && (lo < hi)) hi--;
            if (lo < hi) {
                temp = categoryArr[lo];
                categoryArr[lo] = categoryArr[hi];
                categoryArr[hi] = temp;
            }
        }
        categoryArr[end] = categoryArr[hi];
        categoryArr[hi] = pivot;
        sortDataCategory(start, lo - 1);
        sortDataCategory(hi + 1, end);
    }
