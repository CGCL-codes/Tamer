    public Node removeChild(int n) {
        int numChildren = getChildCount();
        if (n >= numChildren) {
            throw new IllegalArgumentException("Nonexistent child");
        }
        Node[] newChild = new Node[numChildren - 1];
        for (int i = 0; i < n; i++) {
            newChild[i] = child[i];
        }
        for (int i = n; i < numChildren - 1; i++) {
            newChild[i] = child[i + 1];
        }
        Node removed = child[n];
        removed.setParent(null);
        child = newChild;
        return removed;
    }
