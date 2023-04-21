    private int findIndexFor(MutableTreeNode child, MutableTreeNode parent, int i1, int i2) {
        if (i1 == i2) {
            return (comparator.compare(child, parent.getChildAt(i1)) <= 0 ? i1 : i1 + 1);
        }
        int half = (i1 + i2) / 2;
        if (comparator.compare(child, parent.getChildAt(half)) <= 0) {
            return (this.findIndexFor(child, parent, i1, half));
        }
        return (this.findIndexFor(child, parent, half + 1, i1));
    }
