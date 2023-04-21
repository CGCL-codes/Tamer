    private static final void _qsort(Value fxn, Reference[] arr, int lo, int hi) {
        if (lo >= hi) return;
        if (hi == (lo + 1)) {
            if (_compare(fxn, arr[lo], arr[hi]) > 0) _swap(arr, hi, lo);
            return;
        }
        int pivot = (lo + hi) / 2;
        _swap(arr, pivot, hi);
        int bot = lo;
        int top = hi - 1;
        while (bot < top) {
            while ((bot <= top) && (_compare(fxn, arr[bot], arr[hi]) <= 0)) bot++;
            while ((bot <= top) && (_compare(fxn, arr[top], arr[hi]) >= 0)) top--;
            if (bot < top) _swap(arr, top, bot);
        }
        _swap(arr, bot, hi);
        _qsort(fxn, arr, lo, bot - 1);
        _qsort(fxn, arr, bot + 1, hi);
    }
