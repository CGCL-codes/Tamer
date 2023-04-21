    protected void adjustLocations(ClassPane classPane, ClassComponent[] classComponents) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxWeight = 0;
        Set nodes = getLinkedNodeSet();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            Node node = (Node) it.next();
            if (node.getX() > maxX) {
                maxX = node.getX();
            }
            if (node.getX() < minX) {
                minX = node.getX();
            }
            if (node.getY() > maxY) {
                maxY = node.getY();
            }
            if (node.getY() < minY) {
                minY = node.getY();
            }
        }
        double minSize = MIN_DIAGRAM_SIZE;
        if (maxX - minX < minSize) {
            double midX = (maxX + minX) / 2;
            minX = midX - minSize / 2;
            maxX = midX + minSize / 2;
        }
        if (maxY - minY < minSize) {
            double midY = (maxY + minY) / 2;
            minY = midY - minSize / 2;
            maxY = midY + minSize / 2;
        }
        for (Iterator it = edgeMap.keySet().iterator(); it.hasNext(); ) {
            Edge edge = (Edge) it.next();
            double weight = edge.getWeight();
            if (weight > maxWeight) {
                maxWeight = weight;
            }
        }
        int width = (int) Math.round(Math.sqrt(classComponents.length) * 170);
        int height = width;
        double xyRatio = (maxX - minX) / (maxY - minY) / (width / height);
        if (xyRatio > 1) {
            double dy = maxY - minY;
            dy = dy * xyRatio - dy;
            minY = minY - dy / 2;
            maxY = maxY + dy / 2;
        } else if (xyRatio < 1) {
            double dx = maxX - minX;
            dx = dx / xyRatio - dx;
            minX = minX - dx / 2;
            maxX = maxX + dx / 2;
        }
        Dimension classPaneSize = classPane.getSize();
        int xOffset = (width - classPaneSize.width) / 2 + 50;
        height = (int) Math.round(Math.sqrt(classComponents.length) * 3 / 4 * 170);
        int yOffset = (height - classPaneSize.height) / 2 + 25;
        for (int i = 0; i < classComponents.length; i++) {
            ClassComponent classComponent = classComponents[i];
            if (classComponent.isVisible()) {
                Node node = (Node) nodeMap.get(new Node(classComponent, 0, 0));
                int x1 = (int) (width * (node.getX() - minX) / (maxX - minX));
                int y1 = (int) (height * (node.getY() - minY) / (maxY - minY));
                classComponent.setLocation(x1 - xOffset, y1 - yOffset);
            }
        }
        classPane.adjustBounds();
        classPane.revalidate();
        classPane.repaint();
    }
