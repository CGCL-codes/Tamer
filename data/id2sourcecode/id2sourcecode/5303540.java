    private static Entry buildFromSorted(int level, int lo, int hi, int redLevel, Iterator it, java.io.ObjectInputStream str, Object defaultVal) throws java.io.IOException, ClassNotFoundException {
        if (hi < lo) return null;
        int mid = (lo + hi) / 2;
        Entry left = null;
        if (lo < mid) left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
        Object key;
        Object value;
        if (it != null) {
            if (defaultVal == null) {
                Map.Entry entry = (Map.Entry) it.next();
                key = entry.getKey();
                value = entry.getValue();
            } else {
                key = it.next();
                value = defaultVal;
            }
        } else {
            key = str.readObject();
            value = (defaultVal != null ? defaultVal : str.readObject());
        }
        Entry middle = new Entry(key, value, null);
        if (level == redLevel) middle.color = RED;
        if (left != null) {
            middle.left = left;
            left.parent = middle;
        }
        if (mid < hi) {
            Entry right = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
            middle.right = right;
            right.parent = middle;
        }
        return middle;
    }
