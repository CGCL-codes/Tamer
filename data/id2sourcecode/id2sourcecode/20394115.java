    @Override
    protected void scaleShapeRadius(Point previousMousePoint, Point mousePoint) {
        Position referencePos = this.shape.getReferencePosition();
        if (referencePos == null) return;
        Vec4 referencePoint = this.wwd.getModel().getGlobe().computePointFromPosition(referencePos);
        Line screenRay = this.wwd.getView().computeRayFromScreenPoint(mousePoint.getX(), mousePoint.getY());
        Line previousScreenRay = this.wwd.getView().computeRayFromScreenPoint(previousMousePoint.getX(), previousMousePoint.getY());
        Vec4 nearestPointOnLine = screenRay.nearestPointTo(referencePoint);
        Vec4 previousNearestPointOnLine = previousScreenRay.nearestPointTo(referencePoint);
        Position controlPosition = this.controlPoints.get(2).getCenterPosition();
        Vec4 controlPoint = this.wwd.getModel().getGlobe().computePointFromPosition(controlPosition);
        double distance = nearestPointOnLine.distanceTo3(referencePoint);
        double previousDistance = previousNearestPointOnLine.distanceTo3(referencePoint);
        double radiusChange = distance - previousDistance;
        RigidShape shape = this.getShape();
        double eastWestRadius = shape.getEastWestRadius();
        double northSouthRadius = shape.getNorthSouthRadius();
        double average = (eastWestRadius + northSouthRadius) / 2;
        double scalingRatio = (radiusChange + average) / average;
        if (scalingRatio > 0) {
            this.shape.setEastWestRadius(eastWestRadius * scalingRatio);
            this.shape.setNorthSouthRadius(northSouthRadius * scalingRatio);
        }
    }
