    private void setYFirstNode(TreeNode root) {
        ArrayList nodelist = root.getChildrenList();
        int size = nodelist.size();
        TreeNode node;
        int yMin = context.getPicDim().getHeight();
        int yMax = 0;
        for (int i = 0; i < size; i++) {
            int y = ((TreeNode) nodelist.get(i)).getY();
            if (y < yMin) {
                yMin = y;
            }
            if (y > yMax) {
                yMax = y;
            }
        }
        int yMiddle = (yMax + yMin) / 2;
        root.setY(yMiddle);
    }
