    protected void add(Rectangle rect, int l, int r) {
        ensureCapacity(size + 1);
        int idx = l;
        while (l <= r) {
            idx = (l + r) / 2;
            while ((rects[idx] == null) && (idx < r)) idx++;
            if (rects[idx] == null) {
                r = (l + r) / 2;
                idx = (l + r) / 2;
                if (l > r) idx = l;
                while ((rects[idx] == null) && (idx > l)) idx--;
                if (rects[idx] == null) {
                    rects[idx] = rect;
                    return;
                }
            }
            if (rect.x == rects[idx].x) break;
            if (rect.x < rects[idx].x) {
                if (idx == 0) break;
                if ((rects[idx - 1] != null) && (rect.x >= rects[idx - 1].x)) break;
                r = idx - 1;
            } else {
                if (idx == size - 1) {
                    idx++;
                    break;
                }
                if ((rects[idx + 1] != null) && (rect.x <= rects[idx + 1].x)) {
                    idx++;
                    break;
                }
                l = idx + 1;
            }
        }
        if (idx < size) {
            System.arraycopy(rects, idx, rects, idx + 1, size - idx);
        }
        rects[idx] = rect;
        size++;
    }
