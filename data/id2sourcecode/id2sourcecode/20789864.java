    public void printEdgeTag(Graphics canvas, int nodeID, boolean edgyLines) {
        TreeViewNode node = t.treeNodes.get(nodeID);
        FontMetrics fm = canvas.getFontMetrics();
        int width = fm.stringWidth(node.tag) / 2;
        if (node.edgeTag.length() > 0) {
            int x1 = t.treeNodes.get(node.getParent()).x;
            int y1 = t.treeNodes.get(node.getParent()).y + 2;
            int x2 = node.x;
            int y2 = node.y - 10;
            int y = (y1 + y2) / 2 - 10;
            int x = x2;
            if (!edgyLines) {
                x = (x1 + x2) / 2;
            }
            Color color = node.edgeTagColor;
            if (color == null) {
                canvas.setColor(Color.WHITE);
            } else {
                canvas.setColor(color);
            }
            canvas.fillRect(x - width, y, 2 * width, 12);
            canvas.setColor(Color.BLACK);
            canvas.drawRect(x - width, y, 2 * width, 12);
            canvas.drawString(node.edgeTag, x - width / 2, (y1 + y2) / 2);
        }
    }
