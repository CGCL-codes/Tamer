    private boolean isGrouper(String areaName) {
        areaName = areaName.toUpperCase();
        int start = 0;
        int end = root.size() - 1;
        int comp = -1;
        int mid = -1;
        while (start <= end) {
            mid = (end + start) / 2;
            comp = areaName.compareTo((String) root.elementAt(mid, 1));
            if (comp == 0) return true; else if (comp < 0) end = mid - 1; else start = mid + 1;
        }
        return false;
    }
