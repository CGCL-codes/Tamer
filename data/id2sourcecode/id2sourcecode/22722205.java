    private final Entry<K, V> buildFromSorted(int level, int lo, int hi, int redLevel, Iterator it, java.io.ObjectInputStream str, V defaultVal) throws java.io.IOException, ClassNotFoundException {
        if (hi < lo) return null;
        int mid = (lo + hi) / 2;
        Entry<K, V> left = null;
        if (lo < mid) left = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
        K key;
        V value;
        if (it != null) {
            if (defaultVal == null) {
                Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
                key = entry.getKey();
                value = entry.getValue();
            } else {
                key = (K) it.next();
                value = defaultVal;
            }
        } else {
            key = (K) str.readObject();
            value = (defaultVal != null ? defaultVal : (V) str.readObject());
        }
        Entry<K, V> middle = new Entry<K, V>(key, value, null);
        if (level == redLevel) middle.color = RED;
        if (left != null) {
            middle.left = left;
            left.parent = middle;
        }
        if (mid < hi) {
            Entry<K, V> right = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
            middle.right = right;
            right.parent = middle;
        }
        return middle;
    }
