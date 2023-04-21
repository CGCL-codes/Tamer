    private void renderAngle(boolean renderLabel) {
        int zOffset = atomB.screenDiameter + 10;
        int zA = atomA.screenZ - atomA.screenDiameter - 10;
        int zB = atomB.screenZ - zOffset;
        int zC = atomC.screenZ - atomC.screenDiameter - 10;
        int radius = drawLine(atomA.screenX, atomA.screenY, zA, atomB.screenX, atomB.screenY, zB, mad);
        radius += drawLine(atomB.screenX, atomB.screenY, zB, atomC.screenX, atomC.screenY, zC, mad);
        if (!renderLabel) return;
        radius = (radius + 1) / 2;
        AxisAngle4f aa = measurement.getAxisAngle();
        if (aa == null) {
            int offset = (int) (5 * imageFontScaling);
            drawString(atomB.screenX + offset, atomB.screenY - offset, zB, radius, false, false, false, (doJustify ? 0 : Integer.MAX_VALUE), measurement.getString());
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
            pointT.add(atomB);
            Point3i point3iScreenTemp = viewer.transformPoint(pointT);
            int zArc = point3iScreenTemp.z - zOffset;
            if (zArc < 0) zArc = 0;
            g3d.drawPixel(point3iScreenTemp.x, point3iScreenTemp.y, zArc);
            if (i == iMid) {
                pointT.set(ptArc);
                pointT.scale(1.1f);
                matrixT.transform(pointT);
                pointT.add(atomB);
                viewer.transformPoint(pointT);
                int zLabel = point3iScreenTemp.z - zOffset;
                drawString(point3iScreenTemp.x, point3iScreenTemp.y, zLabel, radius, point3iScreenTemp.x < atomB.screenX, false, false, (doJustify ? atomB.screenY : Integer.MAX_VALUE), measurement.getString());
            }
        }
    }
