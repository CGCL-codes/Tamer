    private static void makeBinaryBalancedTree(PhyloTreeNode<Integer> root, int begin, int end) {
        if (begin + 1 == end) {
            root.setName(begin);
            return;
        }
        int mid = (begin + end) / 2;
        if (mid > begin) {
            makeBinaryBalancedTree(root.addChild(), begin, mid);
        }
        if (end > mid) {
            makeBinaryBalancedTree(root.addChild(), mid, end);
        }
    }
