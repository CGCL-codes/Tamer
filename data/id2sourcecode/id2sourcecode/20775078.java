    private static void compute(StoredIntervalsNode<?> node, int q, Set<IInterval> F) {
        int left, right;
        if (node == null || q < (left = node.getLeft()) || q > (right = node.getRight())) {
            return;
        }
        F.addAll(node.intervals());
        int mid = (left + right) / 2;
        if (q < mid) {
            compute((StoredIntervalsNode<?>) node.getLeftSon(), q, F);
        } else if (q >= mid) {
            compute((StoredIntervalsNode<?>) node.getRightSon(), q, F);
        }
    }
