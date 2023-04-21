    public int findInsertPoint(Object element) {
        if (size() == 0) return 0;
        Object entry;
        int high = size();
        int low = -1;
        int index;
        while (high - low > 1) {
            index = (high + low) / 2;
            entry = get(index);
            if (((java.util.Comparator) entry).compare(entry, element) > 0) high = index; else low = index;
        }
        return low == -1 ? 0 : low + 1;
    }
