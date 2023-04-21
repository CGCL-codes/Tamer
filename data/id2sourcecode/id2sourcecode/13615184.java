    public static <T extends Comparable, S extends T> int binarySearch(List<T> list, S value, int[] order) {
        int ret;
        T firstElement = list.get(order[0]);
        T lastElement = list.get(order[list.size() - 1]);
        if (value.compareTo(firstElement) <= 0) {
            ret = (value.compareTo(firstElement) == 0) ? order[0] : -1;
        } else if (value.compareTo(lastElement) > 0) {
            ret = -1;
        } else {
            int a = 0;
            int b = list.size() - 1;
            int middle;
            while (b - a > 1) {
                middle = (a + b) / 2;
                if (value.compareTo(list.get(order[middle])) > 0) {
                    a = middle;
                } else {
                    b = middle;
                }
            }
            ret = (value.compareTo(list.get(order[b])) == 0) ? order[b] : -1;
        }
        return ret;
    }
