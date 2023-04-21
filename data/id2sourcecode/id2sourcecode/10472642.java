    private static BTree<RangeTreeElt> build2dRangeTree(Point[] xP, int left, int right, List<Integer> yOrder) {
        int n = right + 1 - left;
        BTree<RangeTreeElt> t = new BTree<RangeTreeElt>();
        BTreeItr<RangeTreeElt> ti = t.root();
        if (n == 0) return t;
        BSTElt[] yElts = new BSTElt[n];
        for (int i = 0; i < n; i++) yElts[i] = new BSTEltY(xP[yOrder.get(i)]);
        BST<BSTElt> ty = new BST<BSTElt>(yElts);
        int mid = (right + left) / 2;
        ti.insert(new RangeTreeElt(ty, xP[mid]));
        if (n == 1) return t;
        List<Integer> yLeft = new ArrayList<Integer>();
        List<Integer> yRight = new ArrayList<Integer>();
        for (int i : yOrder) {
            if (i < mid) {
                yLeft.add(i);
            } else if (i > mid) {
                yRight.add(i);
            }
        }
        if (right - left > 2) {
            Collections.sort(yLeft, new MyIndexComparator(xP));
            BTree<RangeTreeElt> leftTree = build2dRangeTree(xP, left, mid - 1, yLeft);
            ti.left().paste(leftTree);
        }
        Collections.sort(yRight, new MyIndexComparator(xP));
        BTree<RangeTreeElt> rigthTree = build2dRangeTree(xP, mid + 1, right, yRight);
        ti.right().paste(rigthTree);
        return t;
    }
