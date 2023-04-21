    private void setParentInMiddleOfChildren(TreeNode node, int row) {
        int childrenSize = node.getChildrenList().size();
        int coord_firstChild = 0;
        int coord_lastChild = 0;
        int middle = 0;
        coord_firstChild = ((TreeNode) node.getChildrenList().get(0)).getX() + recDim[row + 1].getWidth();
        coord_lastChild = ((TreeNode) node.getChildrenList().get(childrenSize - 1)).getX();
        middle = (coord_lastChild + coord_firstChild) / 2;
        node.setX(middle - recDim[row].getWidth() / 2);
    }
