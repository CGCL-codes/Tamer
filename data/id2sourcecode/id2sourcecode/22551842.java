    public void drawDescend(int frameNum, float plane, int min, int max, Tree tree) {
        drawInCell(cell.drawer.getColorsForCellGeom(this), plane);
        TreeNode currNode = this;
        int minChild = 0;
        int maxChild = currNode.children.size() - 1;
        int midChild = (minChild + maxChild) / 2;
        TreeNode minLeaf = tree.getLeaf(min + 1);
        TreeNode maxLeaf = tree.getLeaf(max);
        while (!currNode.isLeaf()) {
            TreeNode midNode = currNode.getChild(midChild);
            if (midNode.leftmostLeaf == null || midNode.rightmostLeaf == null) System.out.println("Debug: error, left or rightmost leaf is null");
            if (midNode.leftmostLeaf.key > maxLeaf.key) {
                maxChild = midChild;
            } else if (midNode.rightmostLeaf.key < minLeaf.key) {
                if (maxChild - minChild == 1) minChild = maxChild; else minChild = midChild;
            } else {
                midNode.drawInCell(cell.drawer.getColorsForCellGeom(midNode), plane);
                currNode = midNode;
                minChild = 0;
                maxChild = currNode.children.size() - 1;
            }
            midChild = (minChild + maxChild) / 2;
        }
    }
