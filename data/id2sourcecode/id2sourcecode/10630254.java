    private Point2D constructCollapsedNode(RootedTree tree, Node node, double xPosition, TreeLayoutCache cache) {
        Point2D nodePoint;
        Object[] values = (Object[]) node.getAttribute(collapsedAttributeName);
        double tipHeight = (Double) values[1];
        double height = tree.getHeight(node);
        double maxXPos = xPosition + height - tipHeight;
        double minYPos = yPosition - (yIncrement * 0.5);
        double maxYPos = minYPos + yIncrement;
        yPosition += yIncrement;
        double yPos = (maxYPos + minYPos) / 2;
        nodePoint = new Point2D.Double(xPosition, yPos);
        Point2D transformedNodePoint0 = transform(nodePoint);
        Point2D transformedNodePoint1 = transform(new Point2D.Double(maxXPos, minYPos));
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
        Point2D transformedNodePoint = transform(maxXPos, yPos);
        Line2D tipLabelPath;
        if (tipLabelPosition == TipLabelPosition.FLUSH) {
            tipLabelPath = new Line2D.Double(transformedNodePoint, transform(maxXPos + 1.0, yPos));
        } else if (tipLabelPosition == TipLabelPosition.RADIAL) {
            tipLabelPath = new Line2D.Double(transform(maxXPosition, yPos), transform(maxXPosition + 1.0, yPos));
            Line2D calloutPath = new Line2D.Double(transformedNodePoint, transform(maxXPosition, yPos));
            cache.calloutPaths.put(node, calloutPath);
        } else if (tipLabelPosition == TipLabelPosition.HORIZONTAL) {
            throw new UnsupportedOperationException("Not implemented yet");
        } else {
            throw new IllegalArgumentException("Unrecognized enum value");
        }
        cache.tipLabelPaths.put(node, tipLabelPath);
        cache.nodePoints.put(node, transformedNodePoint0);
        return nodePoint;
    }
