    protected void consumeDataRB(double xRB) {
        BasicGraphData gd = getDataContainerRB();
        if ((gd != null && mpv.getChannel() != null) && (getMeasurement() != -Double.MAX_VALUE)) {
            if (generateUnwrap == false) {
                gd.addPoint(xRB, getMeasurement(), getMeasurementSigma());
            } else {
                int np = gd.getNumbOfPoints();
                double y_last = 0.;
                if (np != 0) {
                    y_last = gd.getY(np - 1);
                }
                double y_new = getMeasurement();
                y_new = unwrap(y_new, y_last);
                gd.addPoint(xRB, y_new, getMeasurementSigma());
            }
        }
    }
