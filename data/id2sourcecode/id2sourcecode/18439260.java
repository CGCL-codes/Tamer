    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        rootLattice.paint(g2, g2.getClipBounds());
        rootLattice.paintLabels(g2, g2.getClipBounds());
        if (selectedArea != null) {
            g2.setPaint(Color.LIGHT_GRAY);
            g2.draw(selectedArea);
        }
        if (showGlobalBottom) {
            Point2D rootPosition = rootLattice.getRootPosition();
            Point2D rootCenter = rootLattice.getCenter();
            double rootScale = rootLattice.getScale();
            double rootRadius = rootLattice.getRadius();
            double nodeRadius = 0.1 * rootScale;
            double startX = rootPosition.getX() + (rootCenter.getX() - rootRadius) * rootScale;
            double endX = rootPosition.getX() + (rootCenter.getX() + rootRadius + 1.8) * rootScale;
            double middleX = (endX + startX) / 2;
            double bottomY = rootPosition.getY() + (rootCenter.getY() + 1.8 + rootRadius) * rootScale + (nodeRadius / 2);
            g2.setPaint(Color.BLACK);
            Line2D globalBottomLine = new Line2D.Double(startX, bottomY + nodeRadius, endX, bottomY + nodeRadius);
            g2.draw(globalBottomLine);
            globalBottomNode.setFrame(middleX - nodeRadius, bottomY, nodeRadius * 2, nodeRadius * 2);
            g2.fill(globalBottomNode);
        }
    }
