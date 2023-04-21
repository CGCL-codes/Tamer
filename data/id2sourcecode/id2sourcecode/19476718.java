    private static <C extends Comparable<? super C>> void siftdown(List<C> a, int n, int vacant, C missing, int drop) {
        int memo = vacant;
        int child, parent;
        int count, next_peek;
        count = 0;
        next_peek = (drop + 1) / 2;
        child = 2 * (vacant + 1);
        while (child < n) {
            if (a.get(child).compareTo(a.get(child - 1)) < 0) child--;
            a.set(vacant, a.get(child));
            vacant = child;
            child = 2 * (vacant + 1);
            count++;
            if (count == next_peek) {
                if (a.get((vacant - 1) / 2).compareTo(missing) <= 0) break; else next_peek = (count + drop + 1) / 2;
            }
        }
        if (child == n) {
            a.set(vacant, a.get(n - 1));
            vacant = n - 1;
        }
        parent = (vacant - 1) / 2;
        while (vacant > memo) {
            if (a.get(parent).compareTo(missing) < 0) {
                a.set(vacant, a.get(parent));
                vacant = parent;
                parent = (vacant - 1) / 2;
            } else break;
        }
        a.set(vacant, missing);
    }
