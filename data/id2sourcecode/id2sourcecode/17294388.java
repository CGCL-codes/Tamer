    private int findIndexFor(MutableTreeNode child, MutableTreeNode parent, int i1, int i2) {
        if (i1 == i2) {
            return comparator.compare((DefaultMutableTreeNode) child, (DefaultMutableTreeNode) parent.getChildAt(i1)) <= 0 ? i1 : i1 + 1;
        }
        int half = (i1 + i2) / 2;
        if (comparator.compare((DefaultMutableTreeNode) child, (DefaultMutableTreeNode) parent.getChildAt(half)) <= 0) {
            return findIndexFor(child, parent, i1, half);
        }
        return findIndexFor(child, parent, half + 1, i2);
    }
