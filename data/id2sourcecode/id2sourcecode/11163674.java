    void paintLines(Graphics g) {
        if (getTransposer().isEnabled()) {
            IFigure node = branch.getNode();
            int left = node.getBounds().right();
            int right = branch.getContentsPane().getBounds().x - 1;
            int yMid = node.getBounds().getCenter().y;
            int xMid = (left + right) / 2;
            List children = getSubtrees();
            if (children.size() == 0) return;
            g.drawLine(left, yMid, xMid, yMid);
            int yMin = yMid;
            int yMax = yMid;
            for (int i = 0; i < children.size(); i++) {
                int y = ((TreeBranch) children.get(i)).getNodeBounds().getCenter().y;
                g.drawLine(xMid, y, right, y);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
            g.drawLine(xMid, yMin, xMid, yMax);
        } else {
            IFigure node = branch.getNode();
            int xMid = node.getBounds().getCenter().x;
            int top = node.getBounds().bottom();
            int bottom = branch.getContentsPane().getBounds().y - 1;
            int yMid = (top + bottom) / 2;
            List children = getSubtrees();
            if (children.size() == 0) return;
            g.drawLine(xMid, top, xMid, yMid);
            int xMin = xMid;
            int xMax = xMid;
            for (int i = 0; i < children.size(); i++) {
                int x = ((TreeBranch) children.get(i)).getNodeBounds().getCenter().x;
                g.drawLine(x, yMid, x, bottom);
                xMin = Math.min(xMin, x);
                xMax = Math.max(xMax, x);
            }
            g.drawLine(xMin, yMid, xMax, yMid);
        }
    }
