    public void qubbleSort(Comparator c, int lo0, int hi0) throws boRuntimeException {
        int lo = lo0;
        int hi = hi0;
        if ((hi - lo) <= 6) {
            bsort(c, lo, hi);
            return;
        }
        Object pivot = null, o1 = null, o2 = null;
        int n = (lo + hi) / 2;
        this.moveTo(n);
        pivot = this.getObject();
        swap(n, hi);
        while (lo < hi) {
            this.moveTo(lo);
            o1 = this.getObject();
            while (c.compare(o1, pivot) <= 0 && lo < hi) {
                lo++;
            }
            this.moveTo(hi);
            o2 = this.getObject();
            while (c.compare(pivot, o2) <= 0 && lo < hi) {
                hi--;
            }
            if (lo < hi) {
                swap(lo, hi);
            }
        }
        swap(hi0, hi);
        qubbleSort(c, lo0, lo - 1);
        qubbleSort(c, hi + 1, hi0);
    }
