    public TreeNode pickDescend(double x, double y, double xFuzz, double yFuzz) {
        Stack pickingStack = new Stack();
        pickingStack.push(this);
        while (pickingStack.size() > 0) {
            TreeNode currRoot = (TreeNode) pickingStack.pop();
            if (currRoot.isNodePicked(x, y, xFuzz, yFuzz)) return currRoot;
            if (currRoot.isLeaf() || !currRoot.xyInRange(y, yFuzz, AccordionDrawer.Y) || currRoot.cell.getMinSplitAbsolute(AccordionDrawer.X) > x) {
                continue;
            }
            int minChild = 0;
            int maxChild = currRoot.children.size() - 1;
            int midChild = (minChild + maxChild + 1) / 2;
            TreeNode currChild = (TreeNode) (currRoot.children.get(midChild));
            while (minChild != maxChild && !currChild.xyInRange(y, 0.0, AccordionDrawer.Y)) {
                if (currChild.cell.getMinSplitAbsolute(AccordionDrawer.Y) > y) maxChild = midChild; else minChild = midChild;
                if (minChild + 1 == maxChild && midChild == minChild) midChild = maxChild; else midChild = (minChild + maxChild) / 2;
                currChild = (TreeNode) (currRoot.children.get(midChild));
            }
            if (currChild.isNodePicked(x, y, xFuzz, yFuzz)) {
                return currChild;
            }
            if (midChild > 0) pickingStack.push(currRoot.children.get(midChild - 1));
            if (midChild < currRoot.children.size() - 1) pickingStack.push(currRoot.children.get(midChild + 1));
            pickingStack.push(currChild);
        }
        return null;
    }
