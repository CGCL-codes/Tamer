    private final ReturnSorted buildFromSorted(int level, int lo, int hi, int redLevel, Iterator it, java.io.ObjectInputStream str, V defaultVal) throws java.io.IOException, ClassNotFoundException {
        if (hi < lo) return null;
        int mid = (lo + hi) / 2;
        Entry<K, V> left = null;
        ReturnSorted ret = null;
        if (lo < mid) {
            ret = buildFromSorted(level + 1, lo, mid - 1, redLevel, it, str, defaultVal);
            left = ret.data;
        }
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
        ReturnSorted st = new ReturnSorted();
        st.leftMost = st.rightMost = middle;
        if (left != null) {
            middle.left = left;
            left.parent = middle;
            middle.previous = ret.rightMost;
            ret.rightMost.next = middle;
            st.leftMost = ret.leftMost;
        }
        if (level == redLevel) middle.color = RED;
        if (mid < hi) {
            ret = buildFromSorted(level + 1, mid + 1, hi, redLevel, it, str, defaultVal);
            Entry<K, V> right = ret.data;
            middle.right = right;
            right.parent = middle;
            st.rightMost = ret.rightMost;
            middle.next = ret.leftMost;
            ret.leftMost.previous = middle;
        }
        st.data = middle;
        return st;
    }
