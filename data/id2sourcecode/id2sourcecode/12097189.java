    private void positionNode(TNNode node, int x, int y, int width, int height) {
        int real_width = Math.min(width, node.getLabelWidth());
        int real_x = x;
        if (real_width < width) {
            real_x = x + (width - real_width) / 2;
            width = real_width;
        }
        Rectangle nodeBounds = new Rectangle(real_x, y, width, height);
        Map nodeAttributes = node.getAttributes();
        GraphConstants.setBounds(nodeAttributes, nodeBounds);
        node.setX(x);
        node.setY(y);
        refreshGraph();
    }
