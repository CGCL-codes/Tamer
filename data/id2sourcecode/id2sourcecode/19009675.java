    public static int searchFirst(Object[] array, int start, int limit, Object value, Comparator c) {
        int low = start;
        int high = limit;
        int mid = start;
        int compare = 0;
        int found = limit;
        while (low < high) {
            mid = (low + high) / 2;
            compare = c.compare(value, array[mid]);
            if (compare < 0) {
                high = mid;
            } else if (compare > 0) {
                low = mid + 1;
            } else {
                high = mid;
                found = mid;
            }
        }
        return found == limit ? -low - 1 : found;
    }
