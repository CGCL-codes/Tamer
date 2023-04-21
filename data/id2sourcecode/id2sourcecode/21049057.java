    private void addTree(Graphics g) {
        Vector<LinkedList<TreeNode<JTextArea>>> levels = doBreadthFirst();
        int y = 0;
        for (int i = 0; i < levels.size(); i++) {
            LinkedList<TreeNode<JTextArea>> level = levels.get(i);
            adjustLevelSizes(level);
            int prevMaxHeight = 0;
            if (i > 0) {
                LinkedList<TreeNode<JTextArea>> prevLevel = levels.get(i - 1);
                prevMaxHeight = (int) getMaxSize(prevLevel).getHeight();
            }
            int nodesInLevel = level.size();
            int totalLevelWidth = 0;
            for (TreeNode<JTextArea> node : level) {
                totalLevelWidth += node.getData().getWidth();
            }
            int sep = (getWidth() - totalLevelWidth) / (nodesInLevel + 1);
            int x = 0;
            y += prevMaxHeight + 20;
            for (TreeNode<JTextArea> node : level) {
                int nodeWidth = node.getData().getWidth();
                if (node.getParent() != null && node.getParent().getChildren().size() == 1) {
                    int parentX = node.getParent().getData().getX();
                    int parentWidth = node.getParent().getData().getWidth();
                    x = parentX + (parentWidth - nodeWidth) / 2;
                } else {
                    x += sep;
                }
                Component data = node.getData();
                data.setLocation(x, y);
                if (i > 0) {
                    Component parentData = node.getParent().getData();
                    int linkX1 = parentData.getX() + parentData.getWidth() / 2;
                    int linkY1 = parentData.getY() + parentData.getHeight() / 2;
                    int linkX2 = data.getX() + data.getWidth() / 2;
                    int linkY2 = data.getY() + data.getHeight() / 2;
                    g.drawLine(linkX1, linkY1, linkX2, linkY2);
                }
                add(data);
                x += nodeWidth;
                height = y + data.getHeight() + 20;
            }
        }
    }
