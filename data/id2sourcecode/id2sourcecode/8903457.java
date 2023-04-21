    private static int getIndexOfClosestValue(int k, List<Integer> ns, int first, int last) {
        if (first == last) {
            return first;
        } else {
            int split = first + (last - first) / 2;
            if (Math.abs(k - ns.get(split)) < Math.abs(k - ns.get(split + 1))) {
                return getIndexOfClosestValue(k, ns, first, split);
            } else {
                return getIndexOfClosestValue(k, ns, split + 1, last);
            }
        }
    }
