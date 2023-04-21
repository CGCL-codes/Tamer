    private Point2D constructCartoonNode(RootedTree tree, Node node, double xPosition, TreeLayoutCache cache) {
        Point2D nodePoint;
        Object[] values = (Object[]) node.getAttribute(cartoonAttributeName);
        int tipCount = (Integer) values[0];
        double tipHeight = (Double) values[1];
        double height = tree.getHeight(node);
        double maxXPos = xPosition + height - tipHeight;
        double minYPos = yPosition;
        yPosition += yIncrement * (tipCount - 1);
        double maxYPos = yPosition;
        yPosition += yIncrement;
        double yPos = (maxYPos + minYPos) / 2;
        nodePoint = new Point2D.Double(xPosition, yPos);
        GeneralPath collapsedShape = new GeneralPath();
        float x0 = (float) nodePoint.getX();
        float y0 = (float) transformY(nodePoint.getY());
        float x1 = (float) maxXPos;
        float y1 = (float) transformY(minYPos);
        float y2 = (float) transformY(maxYPos);
        collapsedShape.moveTo(x0, y0);
        collapsedShape.lineTo(x1, y1);
        collapsedShape.lineTo(x1, y2);
        collapsedShape.closePath();
        cache.collapsedShapes.put(node, collapsedShape);
        Line2D nodeLabelPath = new Line2D.Double(nodePoint.getX(), y0, nodePoint.getX() + 1.0, y0);
        cache.nodeLabelPaths.put(node, nodeLabelPath);
        Line2D nodeBarPath = new Line2D.Double(nodePoint.getX(), y0, nodePoint.getX() - 1.0, y0);
        cache.nodeBarPaths.put(node, nodeBarPath);
        if (showingCartoonTipLabels) {
            constructCartoonTipLabelPaths(tree, node, maxXPos, new double[] { minYPos }, cache);
        }
        return nodePoint;
    }
