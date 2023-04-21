    private TravResult searchToLeafPos(StructuralIdentifier key, BTreeNode currentRoot) {
        BTreeNode node = currentRoot.getBTreeNode(key);
        while (node instanceof InternalNode) {
            node = node.getBTreeNode(key);
        }
        Leaf leaf = (Leaf) node;
        Vector keys = leaf.entries.keys;
        int l = 0;
        int r = keys.size();
        int m = (l + r) / 2;
        int prevm = 0;
        int prevprevm = 0;
        while (m < keys.size() && m >= 0 && prevm != m && prevprevm != m) {
            StructuralIdentifier mdid = (StructuralIdentifier) keys.elementAt(m);
            int posC = mdid.compare(key);
            if (posC > 0) {
                prevprevm = prevm;
                prevm = m;
                l = m;
            } else if (posC < 0) {
                prevprevm = prevm;
                prevm = m;
                r = m;
            } else {
                return new TravResult(leaf, m, true);
            }
            m = (l + r) / 2;
        }
        return new TravResult(leaf, m, false);
    }
