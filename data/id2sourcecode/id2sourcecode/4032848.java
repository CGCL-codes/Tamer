    void renderAngle(Atom atomA, Atom atomB, Atom atomC, short colix, boolean renderArcs) {
        if (displayModelIndex >= 0 && (displayModelIndex != atomA.modelIndex || displayModelIndex != atomB.modelIndex || displayModelIndex != atomC.modelIndex)) return;
        g3d.setColix(colix);
        int zA = atomA.getScreenZ() - atomA.getScreenD() - 10;
        int zB = atomB.getScreenZ() - atomB.getScreenD() - 10;
        int zC = atomC.getScreenZ() - atomC.getScreenD() - 10;
        int zOffset = (zA + zB + zC) / 3;
        int radius = drawSegment(atomA.getScreenX(), atomA.getScreenY(), zA, atomB.getScreenX(), atomB.getScreenY(), zB, colix);
        radius += drawSegment(atomB.getScreenX(), atomB.getScreenY(), zB, atomC.getScreenX(), atomC.getScreenY(), zC, colix);
        radius = (radius + 1) / 2;
        if (!renderArcs) return;
        AxisAngle4f aa = measurement.aa;
        if (aa == null) {
            paintMeasurementString(atomB.getScreenX() + 5, atomB.getScreenY() - 5, zB, radius, colix);
            return;
        }
        int dotCount = (int) ((aa.angle / (2 * Math.PI)) * 64);
        float stepAngle = aa.angle / dotCount;
        aaT.set(aa);
        int iMid = dotCount / 2;
        for (int i = dotCount; --i >= 0; ) {
            aaT.angle = i * stepAngle;
            matrixT.set(aaT);
            pointT.set(measurement.pointArc);
            matrixT.transform(pointT);
            pointT.add(atomB.point3f);
            Point3i screenArc = viewer.transformPoint(pointT);
            int zArc = screenArc.z - zOffset;
            if (zArc < 0) zArc = 0;
            g3d.drawPixel(screenArc.x, screenArc.y, zArc);
            if (i == iMid) {
                pointT.set(measurement.pointArc);
                pointT.scale(1.1f);
                matrixT.transform(pointT);
                pointT.add(atomB.point3f);
                Point3i screenLabel = viewer.transformPoint(pointT);
                int zLabel = screenLabel.z - zOffset;
                paintMeasurementString(screenLabel.x, screenLabel.y, zLabel, radius, colix);
            }
        }
    }
