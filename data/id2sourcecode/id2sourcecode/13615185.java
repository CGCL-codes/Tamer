    public static <T, S extends T> int binarySearchRT(List<T> list, S value, int[] order) throws IllegalArgumentException {
        int ret;
        try {
            Comparable cvalue = (Comparable) value;
            T firstElement = list.get(order[0]);
            T lastElement = list.get(order[list.size() - 1]);
            if (cvalue.compareTo(firstElement) <= 0) {
                ret = (cvalue.compareTo(firstElement) == 0) ? order[0] : -1;
            } else if (cvalue.compareTo(lastElement) > 0) {
                ret = -1;
            } else {
                int a = 0;
                int b = list.size() - 1;
                int middle;
                while (b - a > 1) {
                    middle = (a + b) / 2;
                    if (cvalue.compareTo(list.get(order[middle])) > 0) {
                        a = middle;
                    } else {
                        b = middle;
                    }
                }
                ret = (cvalue.compareTo(list.get(order[b])) == 0) ? order[b] : -1;
            }
        } catch (Exception e) {
            ret = -1;
            throw new IllegalArgumentException("CollectionUtils: Cannot complete binary search: Given list elements not comparable", e);
        }
        return ret;
    }
