    public boolean addSort(Field element) {
        int low = 0;
        int high = sorted.size();
        int mid = (low + high) / 2;
        int oldmid = mid;
        int comp;
        int count = 0;
        do {
            oldmid = mid;
            count++;
            comp = element.compareTo(sorted.get(mid));
            if (comp > 0) {
                low = mid;
            } else if (comp < 0) {
                high = mid;
            } else return false;
        } while ((mid = (low + high) / 2) != oldmid);
        if (comp > 0) {
            sorted.add(mid + 1, element);
        } else sorted.add(mid, element);
        return true;
    }
