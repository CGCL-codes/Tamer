    public int findIndexForChild(VPTNode child) {
        if (children == null || children.size() == 0) return 0;
        VPTNodeComparator c = new VPTNodeComparator();
        int b = 0, e = children.size(), i = e / 2;
        VPTNode n;
        while (e - b > 1) {
            n = (VPTNode) children.get(i);
            int comp = c.compare(child, n);
            if (comp < 0) {
                e = i;
            } else if (comp == 0) {
                i++;
                b = e = i;
            } else {
                b = i;
            }
            i = (e + b) / 2;
        }
        if (b == children.size()) return b;
        n = (VPTNode) children.get(b);
        return (c.compare(child, n) < 0 ? b : b + 1);
    }
