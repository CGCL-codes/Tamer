    public BTreeNode getBTreeNode(StructuralIdentifier key) {
        int l = 0;
        int r = entries.size();
        int m = (l + r) / 2;
        int prevm = 0;
        int prevprevm = 0;
        StructuralIdentifier did = (StructuralIdentifier) entries.keys.elementAt(m);
        while (m < entries.size() && m > 0 && prevm != m && prevprevm != m) {
            did = (StructuralIdentifier) entries.keys.elementAt(m);
            if ((did).compare(key) < 0) {
                prevprevm = prevm;
                prevm = m;
                r = m;
            } else if ((did).compare(key) > 0) {
                prevprevm = prevm;
                prevm = m;
                l = m;
            } else {
                return this.getNode(m + 1);
            }
            m = (l + r) / 2;
        }
        if (m == 0) {
            did = (StructuralIdentifier) entries.keys.elementAt(0);
            if (key.compare(did) == 1) {
                return this.getNode(0);
            } else if (did.compare(key) == 1) {
                return this.getNode(1);
            } else {
                return this.getNode(1);
            }
        }
        if (prevm == m) {
            if (key.compare(did) == 1) {
                return this.getNode(m);
            } else if (did.compare(key) == 1) {
                return this.getNode(m + 1);
            }
        }
        if (prevprevm == m) {
            did = (StructuralIdentifier) entries.keys.elementAt(m);
            if (key.compare(did) == 1) {
                return this.getNode(m);
            } else if (did.compare(key) == 1) {
                return this.getNode(m + 1);
            }
        }
        return null;
    }
