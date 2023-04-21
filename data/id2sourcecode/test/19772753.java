    public SegmentTreeNode<T> getNode(int target) {
        if (left == target) {
            if (right == left + 1) {
                return this;
            }
        }
        int mid = (left + right) / 2;
        if (target < mid) {
            return lson.getNode(target);
        }
        return rson.getNode(target);
    }
