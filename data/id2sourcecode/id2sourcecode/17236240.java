    private double paintBoxPlot(Graphics2D g2, Tree tree, NodeRef node, double x1, boolean fill) {
        double y;
        double iy;
        if (tree.isExternal(node)) {
            if (rememberYPositions) {
                String taxonId = tree.getNodeTaxon(node).getId();
                Double pos = yPositionMap.get(taxonId);
                if (pos != null) {
                    y = pos;
                } else {
                    y = currentY;
                    currentY += 1.0;
                    yPositionMap.put(taxonId, y);
                }
            } else {
                y = currentY;
                currentY += 1.0;
            }
            iy = convertY(y);
        } else {
            double y0, y1;
            NodeRef child = tree.getChild(node, 0);
            double length = tree.getNodeHeight(node) - tree.getNodeHeight(child);
            y0 = paintBoxPlot(g2, tree, child, x1 - length, fill);
            y1 = y0;
            for (int i = 1; i < tree.getChildCount(node); i++) {
                child = tree.getChild(node, i);
                length = tree.getNodeHeight(node) - tree.getNodeHeight(child);
                y1 = paintBoxPlot(g2, tree, child, x1 - length, fill);
            }
            y = (y1 + y0) / 2;
            iy = convertY(y);
        }
        Double mean = (Double) tree.getNodeAttribute(node, "nodeHeight.mean");
        if (mean != null) {
            if (tree.isRoot(node)) {
                System.out.println(mean.doubleValue());
            }
            Double hpdUpper = (Double) tree.getNodeAttribute(node, "nodeHeight.hpdUpper");
            Double hpdLower = (Double) tree.getNodeAttribute(node, "nodeHeight.hpdLower");
            double upperX = convertX(hpdUpper);
            double lowerX = convertX(hpdLower);
            g2.setStroke(lineStroke);
            if (fill) {
                g2.setColor(Color.white);
                g2.fill(new Rectangle2D.Double(upperX, iy - boxPlotSize, lowerX - upperX, 2 * boxPlotSize));
            }
            g2.setColor(Color.gray);
            g2.draw(new Rectangle2D.Double(upperX, iy - boxPlotSize, lowerX - upperX, 2 * boxPlotSize));
        }
        return y;
    }
