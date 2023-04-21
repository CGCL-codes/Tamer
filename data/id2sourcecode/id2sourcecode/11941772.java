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
        double ty = transformY(yPos);
        GeneralPath collapsedShape = new GeneralPath();
        float x0 = (float) nodePoint.getX();
        float y0 = (float) ty;
        float x1 = (float) maxXPos;
        float y1 = (float) transformY(minYPos);
        float y2 = (float) transformY(maxYPos);
        collapsedShape.moveTo(x0, y0);
        collapsedShape.lineTo(x1, y1);
        collapsedShape.lineTo(x1, y2);
        collapsedShape.closePath();
        cache.collapsedShapes.put(node, collapsedShape);
        Line2D nodeLabelPath = new Line2D.Double(xPosition, ty, xPosition + 1.0, ty);
        cache.nodeLabelPaths.put(node, nodeLabelPath);
        Line2D nodeBarPath = new Line2D.Double(xPosition, ty, xPosition - 1.0, ty);
        cache.nodeBarPaths.put(node, nodeBarPath);
        Line2D tipLabelPath;
        if (alignTipLabels) {
            tipLabelPath = new Line2D.Double(maxXPosition, ty, maxXPosition + 1.0, ty);
            Line2D calloutPath = new Line2D.Double(maxXPos, ty, maxXPosition, ty);
            cache.calloutPaths.put(node, calloutPath);
        } else {
            tipLabelPath = new Line2D.Double(maxXPos, ty, maxXPos + 1.0, ty);
        }
        cache.tipLabelPaths.put(node, tipLabelPath);
        return nodePoint;
    }
