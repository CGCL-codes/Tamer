    private final void qsort(int[] array, float[][] samples, int sIndex, int lo, int hi) {
        if (lo < hi) {
            int pivot = (lo + hi) / 2;
            int swap = array[lo];
            array[lo] = array[pivot];
            array[pivot] = swap;
            pivot = lo;
            for (int i = lo + 1; i <= hi; i++) if (samples[sIndex][array[i]] < samples[sIndex][array[lo]]) {
                swap = array[i];
                array[i] = array[++pivot];
                array[pivot] = swap;
            }
            swap = array[lo];
            array[lo] = array[pivot];
            array[pivot] = swap;
            if (lo < pivot - 1) qsort(array, samples, sIndex, lo, pivot - 1);
            if (pivot + 1 < hi) qsort(array, samples, sIndex, pivot + 1, hi);
        }
    }
