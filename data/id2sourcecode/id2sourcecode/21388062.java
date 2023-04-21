    private static BTree optimalBST(Comparable[] sorted, int left, int right) {
        BTree r = new BTree();
        BTreeItr ri = r.root();
        int middleIndex = (right + left) / 2;
        ri.insert(sorted[middleIndex]);
        if (left < middleIndex) {
            ri.left().paste(optimalBST(sorted, left, middleIndex - 1));
        }
        if (right > middleIndex) {
            ri.right().paste(optimalBST(sorted, middleIndex + 1, right));
        }
        return r;
    }
