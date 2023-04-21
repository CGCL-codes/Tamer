    protected void consumeData(double x) {
        BasicGraphData gd = getDataContainer();
        if ((gd != null && mpv.getChannel() != null) && (getMeasurement() != -Double.MAX_VALUE)) {
            if (generateUnwrap == false) {
                gd.addPoint(x, getMeasurement(), getMeasurementSigma());
            } else {
                int np = gd.getNumbOfPoints();
                double y_last = 0.;
                if (np != 0) {
                    y_last = gd.getY(np - 1);
                }
                double y_new = getMeasurement();
                y_new = unwrap(y_new, y_last);
                gd.addPoint(x, y_new, getMeasurementSigma());
            }
        }
    }
