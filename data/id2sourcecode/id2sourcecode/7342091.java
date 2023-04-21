    private int getSimilarestIndex(E e, Boolean smallerSimilarest, int min, int max) {
        if (list.isEmpty()) return -1;
        sortNow();
        int current;
        while (min + 1 != max) {
            current = (min + max) / 2;
            int cmp = list.get(current).compareTo(e);
            if (cmp < 0) min = current; else if (cmp > 0) max = current; else return current;
        }
        if (smallerSimilarest) {
            return min;
        } else {
            if (max >= list.size()) return -1;
            return max;
        }
    }
