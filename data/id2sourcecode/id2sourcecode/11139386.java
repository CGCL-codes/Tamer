    protected int indexOf(Comparable target) {
        Comparable midValue;
        int low = 0;
        int high = data.size();
        int mid = (low + high) / 2;
        while (low < high) {
            midValue = (Comparable) data.get(mid);
            if (midValue.compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid;
            }
            mid = (low + high) / 2;
        }
        return low;
    }
