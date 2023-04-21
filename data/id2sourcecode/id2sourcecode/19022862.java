    public static GeneralPath getPath(DasAxis xAxis, DasAxis yAxis, QDataSet xds, QDataSet yds, boolean histogram, boolean clip) {
        GeneralPath newPath = new GeneralPath();
        Dimension d;
        Units xUnits = SemanticOps.getUnits(xds);
        Units yUnits = SemanticOps.getUnits(yds);
        QDataSet tagds = SemanticOps.xtagsDataSet(xds);
        double xSampleWidth = Double.MAX_VALUE;
        if (tagds.property(QDataSet.CADENCE) != null) {
        }
        double i0 = -Double.MAX_VALUE;
        double j0 = -Double.MAX_VALUE;
        boolean v0 = false;
        boolean skippedLast = true;
        int n = xds.length();
        QDataSet wds = SemanticOps.weightsDataSet(yds);
        Rectangle rclip = clip ? DasDevicePosition.toRectangle(yAxis.getRow(), xAxis.getColumn()) : null;
        for (int index = 0; index < n; index++) {
            double t = index;
            double x = xds.value(index);
            double y = yds.value(index);
            double i = xAxis.transform(x, xUnits);
            double j = yAxis.transform(y, yUnits);
            boolean v = rclip == null || rclip.contains(i, j);
            if (wds.value(index) == 0 || Double.isNaN(y)) {
                skippedLast = true;
            } else if (skippedLast) {
                newPath.moveTo((float) i, (float) j);
                skippedLast = !v;
            } else {
                if (v || v0) {
                    if (histogram) {
                        double i1 = (i0 + i) / 2;
                        newPath.lineTo((float) i1, (float) j0);
                        newPath.lineTo((float) i1, (float) j);
                        newPath.lineTo((float) i, (float) j);
                    } else {
                        newPath.lineTo((float) i, (float) j);
                    }
                }
                skippedLast = false;
            }
            i0 = i;
            j0 = j;
            v0 = v;
        }
        return newPath;
    }
