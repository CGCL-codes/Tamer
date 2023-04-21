    private Collection<GMLBuilding> divide(GMLBuilding b) {
        Collection<GMLBuilding> result = new HashSet<GMLBuilding>();
        List<Point2D> vertices = coordinatesToVertices(b.getUnderlyingCoordinates());
        double area = GeometryTools2D.computeArea(vertices);
        if (area <= minSize) {
            result.add(b);
        } else {
            if (area > maxSize || split.nextEvent(config.getRandom())) {
                double xMin = b.getBounds().getMinX();
                double xMax = b.getBounds().getMaxX();
                double yMin = b.getBounds().getMinY();
                double yMax = b.getBounds().getMaxY();
                double width = xMax - xMin;
                double height = yMax - yMin;
                if (height > width) {
                    double splitY = (yMax + yMin) / 2;
                    double topOffset = separationGenerator.nextValue();
                    double bottomOffset = separationGenerator.nextValue();
                    double topY = splitY + topOffset;
                    double bottomY = splitY - bottomOffset;
                    if (yMax - topY < minHeight || bottomY - yMin < minHeight) {
                        result.add(b);
                    } else {
                        result.addAll(divide(createBuilding(xMin, yMin, xMax, bottomY)));
                        result.addAll(divide(createBuilding(xMin, topY, xMax, yMax)));
                    }
                } else {
                    double splitX = (xMax + xMin) / 2;
                    double leftOffset = separationGenerator.nextValue();
                    double rightOffset = separationGenerator.nextValue();
                    double leftX = splitX - leftOffset;
                    double rightX = splitX + rightOffset;
                    if (xMax - rightX < minWidth || leftX - xMin < minWidth) {
                        result.add(b);
                    } else {
                        result.addAll(divide(createBuilding(xMin, yMin, leftX, yMax)));
                        result.addAll(divide(createBuilding(rightX, yMin, xMax, yMax)));
                    }
                }
            } else {
                result.add(b);
            }
        }
        return result;
    }
