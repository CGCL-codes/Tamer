    private static void mergeSortAlphabetique(Sort[] a, Sort[] tmpArray, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSortAlphabetique(a, tmpArray, left, center);
            mergeSortAlphabetique(a, tmpArray, center + 1, right);
            mergeSortAlphabetique(a, tmpArray, left, center + 1, right);
        }
    }
