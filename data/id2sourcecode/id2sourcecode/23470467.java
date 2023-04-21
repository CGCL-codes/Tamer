    private static int get_index(MAQRyanMap[] map, int value) {
        int low = 0;
        int high = map.length - 1;
        int lastMid = -1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (map[mid].get_fa_coord_st() < value && map[mid].get_fa_coord_end() > value) {
                return mid;
            } else {
                if (map[mid].get_fa_coord_st() < value) {
                    low = mid;
                } else {
                    high = mid;
                }
            }
            if (lastMid == mid) {
                break;
            }
            lastMid = mid;
        }
        return -1;
    }
