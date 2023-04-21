    public boolean contains(Object o) {
        Rectangle rect = (Rectangle) o;
        int l = 0, r = size - 1, idx = 0;
        while (l <= r) {
            idx = (l + r) / 2;
            if (rect.x == rects[idx].x) break;
            if (rect.x < rects[idx].x) {
                if (idx == 0) break;
                if (rect.x >= rects[idx - 1].x) break;
                r = idx - 1;
            } else {
                if (idx == size - 1) {
                    idx++;
                    break;
                }
                if (rect.x <= rects[idx + 1].x) {
                    idx++;
                    break;
                }
                l = idx + 1;
            }
        }
        if (rects[idx].x != rect.x) return false;
        for (int i = idx; i >= 0; i--) {
            if (rects[idx].equals(rect)) return true;
            if (rects[idx].x != rect.x) break;
        }
        for (int i = idx + 1; i < size; i++) {
            if (rects[idx].equals(rect)) return true;
            if (rects[idx].x != rect.x) break;
        }
        return false;
    }
