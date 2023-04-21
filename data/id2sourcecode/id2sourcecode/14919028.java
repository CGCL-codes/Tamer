    public int roomCount(String areaName) {
        int x = areaName.indexOf("#");
        if (x > 0) areaName = areaName.substring(0, x).toUpperCase(); else areaName = areaName.toUpperCase();
        int start = 0;
        int end = root.size() - 1;
        int comp = -1;
        int mid = -1;
        while (start <= end) {
            mid = (end + start) / 2;
            comp = areaName.compareTo((String) root.elementAt(mid, 1));
            if (comp == 0) {
                if (root.elementAt(mid, 2) != null) {
                    CMIntegerGrouper CMI = (CMIntegerGrouper) root.elementAt(mid, 2);
                    if (CMI == null) return 1;
                    return CMI.roomCount();
                }
                return 0;
            } else if (comp < 0) end = mid - 1; else start = mid + 1;
        }
        return 0;
    }
