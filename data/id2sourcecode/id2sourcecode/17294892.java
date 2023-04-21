    private void renderAngle(boolean renderLabel) {
        int zOffset = nodeB.screenDiameter + 10;
        int zA = nodeA.screenZ - nodeA.screenDiameter - 10;
        int zB = nodeB.screenZ - zOffset;
        int zC = nodeC.screenZ - nodeC.screenDiameter - 10;
        int radius = drawSegment(nodeA.screenX, nodeA.screenY, zA, nodeB.screenX, nodeB.screenY, zB);
        radius += drawSegment(nodeB.screenX, nodeB.screenY, zB, nodeC.screenX, nodeC.screenY, zC);
        if (!renderLabel) return;
        radius = (radius + 1) / 2;
        AxisAngle4f aa = measurement.getAxisAngle();
        if (aa == null) {
            int offset = (int) (5 * imageFontScaling);
            paintMeasurementString(nodeB.screenX + offset, nodeB.screenY - offset, zB, radius, false, 0);
            return;
        }
        int dotCount = (int) ((aa.angle / (2 * Math.PI)) * 64);
        float stepAngle = aa.angle / dotCount;
        aaT.set(aa);
        int iMid = dotCount / 2;
        Point3f ptArc = measurement.getPointArc();
        for (int i = dotCount; --i >= 0; ) {
            aaT.angle = i * stepAngle;
            matrixT.set(aaT);
            pointT.set(ptArc);
            matrixT.transform(pointT);
            pointT.add(nodeB);
            Point3i point3iScreenTemp = viewer.transformPoint(pointT);
            int zArc = point3iScreenTemp.z - zOffset;
            if (zArc < 0) zArc = 0;
            g3d.drawPixel(point3iScreenTemp.x, point3iScreenTemp.y, zArc);
            if (i == iMid) {
                pointT.set(ptArc);
                pointT.scale(1.1f);
                matrixT.transform(pointT);
                pointT.add(nodeB);
                viewer.transformPoint(pointT);
                int zLabel = point3iScreenTemp.z - zOffset;
                paintMeasurementString(point3iScreenTemp.x, point3iScreenTemp.y, zLabel, radius, point3iScreenTemp.x < nodeB.screenX, nodeB.screenY);
            }
        }
    }
