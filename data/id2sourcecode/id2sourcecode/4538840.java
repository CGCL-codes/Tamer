    public int findSort(String key) {
        int low = 0;
        int high = sorted.size();
        int mid = (low + high) / 2;
        int oldmid = -2;
        int comp;
        while ((mid = (high + low) / 2) != oldmid) {
            comp = sorted.get(mid).compareTo(key);
            if (comp < 0) {
                low = mid;
            } else if (comp > 0) {
                high = mid;
            } else return mid;
            oldmid = mid;
        }
        return -1;
    }
