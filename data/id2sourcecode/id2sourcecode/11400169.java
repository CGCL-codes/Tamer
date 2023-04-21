    public static int exonLowerBoundBinarySearch(Exon[] a, int x) {
        int low = 0;
        int high = a.length - 1;
        int mid = -1;
        while (low < high - 1) {
            mid = (low + high) / 2;
            if (a[mid].getLocation().getStart() < x - SEARCH_WINDOW) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low;
    }
