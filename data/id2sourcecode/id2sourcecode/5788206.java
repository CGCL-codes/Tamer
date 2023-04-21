    void sort_aux(int head, int tail) {
        if (head < tail) {
            int pivotIndex = (head + tail) / 2;
            Object pivot = v.elementAt(pivotIndex);
            int i = head - 1;
            int j = tail + 1;
            do {
                do i++; while (!leq(pivot, v.elementAt(i)));
                do j--; while (!leq(v.elementAt(j), pivot));
                if (i < j) swap(i, j);
            } while (i < j);
            if (i == j) {
                sort_aux(head, j - 1);
                sort_aux(i + 1, tail);
            } else {
                sort_aux(head, j);
                sort_aux(i, tail);
            }
        }
    }
