    private void sort(int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        SortableItem mid;
        if (hi0 > lo0) {
            int nIndex = (lo0 + hi0) / 2;
            mid = m_arr.get(nIndex);
            while (lo <= hi) {
                while ((lo < hi0) && (m_arr.get(lo).compare(mid) < 0)) ++lo;
                while ((hi > lo0) && (m_arr.get(hi).compare(mid) > 0)) --hi;
                if (lo <= hi) {
                    swap(lo, hi);
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) sort(lo0, hi);
            if (lo < hi0) sort(lo, hi0);
        }
    }
