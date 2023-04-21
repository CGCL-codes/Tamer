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
        Point2D transformedNodePoint0 = transform(nodePoint);
        Point2D transformedNodePoint1 = transform(new Point2D.Double(maxXPos, minYPos));
        Point2D transformedNodePoint2 = transform(new Point2D.Double(maxXPos, maxYPos));
        GeneralPath collapsedShape = new GeneralPath();
        collapsedShape.moveTo((float) transformedNodePoint0.getX(), (float) transformedNodePoint0.getY());
        collapsedShape.lineTo((float) transformedNodePoint1.getX(), (float) transformedNodePoint1.getY());
        final double start = getAngle(maxYPos);
        final double finish = getAngle(minYPos);
        Arc2D arc = new Arc2D.Double();
        arc.setArcByCenter(0.0, 0.0, maxXPos, finish, start - finish, Arc2D.OPEN);
        collapsedShape.append(arc, true);
        collapsedShape.closePath();
        cache.collapsedShapes.put(node, collapsedShape);
        Line2D nodeLabelPath = new Line2D.Double(transform(nodePoint.getX(), yPos), transform(nodePoint.getX() + 1.0, yPos));
        cache.nodeLabelPaths.put(node, nodeLabelPath);
        Line2D nodeBarPath = new Line2D.Double(transform(nodePoint.getX(), yPos), transform(nodePoint.getX() - 1.0, yPos));
        cache.nodeBarPaths.put(node, nodeBarPath);
        if (showingCartoonTipLabels) {
            constructCartoonTipLabelPaths(tree, node, maxXPos, new double[] { minYPos }, cache);
        }
        cache.nodePoints.put(node, transformedNodePoint0);
        return nodePoint;
    }
