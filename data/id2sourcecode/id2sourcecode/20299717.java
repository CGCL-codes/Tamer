    public static <E> void quickSort(List<E> data, int l, int r, Comparator<? super E> comparator) {
        int re, li, mid;
        E tmp;
        if (r - l < 10) for (li = l + 1; li <= r; li++) {
            tmp = data.get(li);
            for (re = li; re > l && comparator.compare(tmp, data.get(re - 1)) < 0; re--) data.set(re, data.get(re - 1));
            data.set(re, tmp);
        } else while (l < r) {
            li = l;
            re = r;
            mid = (li + re) / 2;
            if (comparator.compare(data.get(li), data.get(mid)) > 0) swap(data, li, mid);
            if (comparator.compare(data.get(mid), data.get(re)) > 0) swap(data, mid, re);
            if (comparator.compare(data.get(li), data.get(mid)) > 0) swap(data, li, mid);
            tmp = data.get(mid);
            while (li <= re) {
                while (comparator.compare(data.get(li), tmp) < 0) li++;
                while (comparator.compare(tmp, data.get(re)) < 0) re--;
                if (li <= re) {
                    swap(data, li, re);
                    li++;
                    re--;
                }
            }
            if (l < re) quickSort(data, l, re, comparator);
            l = li;
        }
    }
