    @Override
    public NgramNode getChild(int id, int add) {
        NgramNode child = null;
        int bot = 0, top, mid = 0;
        if (children != null) {
            if (parent == null) {
                while (id >= children.size()) children.add(null);
                child = children.get(id);
            } else {
                top = children.size();
                while (bot < top) {
                    mid = (bot + top) / 2;
                    child = children.get(mid);
                    if (child.id > id) top = mid; else if (child.id < id) bot = ++mid; else return child;
                }
                child = null;
            }
        }
        if (child == null && add != ADD_NONE) {
            if (children == null) children = new Vector<NgramNode>(1);
            child = (add == ADD_LEAF ? new NgramNode(id, this) : new BranchNode(id, this));
            if (parent == null) children.set(id, child); else children.insertElementAt(child, mid);
            childCount++;
        }
        return child;
    }
