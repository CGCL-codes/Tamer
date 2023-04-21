    private <T> void qsort(Container<T> data, IComparator<T> comparator, int low, int high, int wanted) {
        if (low >= high) {
            return;
        }
        T ot = null;
        T or = null;
        T op = null;
        if (low == high - 1) {
            ot = data.getAt(low);
            if (comparator.compare(ot, data.getAt(high)) == wanted) {
                or = data.getAt(high);
                data.setAt(high, null);
                data.setAt(low, or);
                data.setAt(high, ot);
            }
            return;
        }
        int l = low;
        int h = high;
        int p = (l + h) / 2;
        op = data.getAt(p);
        ot = data.getAt(h);
        data.setAt(h, null);
        data.setAt(p, ot);
        data.setAt(h, op);
        while (l < h) {
            while (comparator.compare(op, data.getAt(l)) == wanted || comparator.compare(op, data.getAt(l)) == IComparator.EQUAL && l < h) {
                l++;
            }
            while (comparator.compare(data.getAt(h), op) == wanted || comparator.compare(op, data.getAt(l)) == IComparator.EQUAL && l < h) {
                h--;
            }
            if (l < h) {
                ot = data.getAt(l);
                or = data.getAt(h);
                data.setAt(h, null);
                data.setAt(l, or);
                data.setAt(h, ot);
            }
        }
        qsort(data, comparator, low, l - 1, wanted);
        qsort(data, comparator, h + 1, high, wanted);
    }
