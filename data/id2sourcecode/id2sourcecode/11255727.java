    private void calculateCoordinates() throws InternalException {
        double innerRad = (double) innerRadius;
        double outerRad = (double) outerRadius;
        double startAng = (double) startAngle;
        double stopAng = (double) stopAngle;
        double dlat = 0;
        double dlong = 0;
        double angle;
        if (stopAngle < startAngle) {
            angle = (startAngle + (stopAngle + 360)) / 2;
            if (angle > 360) angle -= 360;
        } else angle = (startAng + stopAng) / 2;
        radius = (outerRad + innerRad) / 2;
        double v = 0;
        double deltaX = 0;
        double deltaY = 0;
        try {
            dlat = PositioningUtil.convertLLToDecimal(cellLatitude);
            dlong = PositioningUtil.convertLLToDecimal(cellLongitude);
        } catch (PositioningAPIException nfe) {
            LogServiceManager.getLog("positioningAPI, MPSPositioningData").println(Log.ERROR, "Error when parsing coordinates to double", nfe);
            throw new InternalException("MPSPositioningData: Unable to convert coordinates to decimal values, " + nfe.getErrorMessage());
        }
        if (cellLatitude.charAt(0) == 'S') dlat = -dlat;
        if (cellLongitude.charAt(0) == 'W') dlong = -dlong;
        if (angle == 0 || angle == 360) {
            deltaX = 0;
            deltaY = radius;
        } else if (angle == 90) {
            deltaX = radius;
            deltaY = 0;
        } else if (angle == 180) {
            deltaX = 0;
            deltaY = -radius;
        } else if (angle == 270) {
            deltaX = -radius;
            deltaY = 0;
        } else {
            if ((angle > 0) && (angle < 90)) {
                v = angle;
                deltaX = (Math.sin(Math.toRadians(v)) * radius);
                deltaY = (Math.sin(Math.toRadians(90 - v)) * radius);
            } else if ((angle > 90) && (angle < 180)) {
                v = angle - 90;
                deltaY = -(Math.sin(Math.toRadians(v)) * radius);
                deltaX = (Math.sin(Math.toRadians(90 - v)) * radius);
            } else if ((angle > 180) && (angle < 270)) {
                v = angle - 180;
                deltaX = -(Math.sin(Math.toRadians(v)) * radius);
                deltaY = -(Math.sin(Math.toRadians(90 - v)) * radius);
            } else {
                v = angle - 270;
                deltaY = (Math.sin(Math.toRadians(v)) * radius);
                deltaX = -(Math.sin(Math.toRadians(90 - v)) * radius);
            }
        }
        decimalLatitude = dlat + (deltaY / conversionConstant);
        decimalLongitude = dlong + (deltaX / (Math.cos(Math.toRadians(decimalLatitude)) * conversionConstant));
        latitude = PositioningUtil.convertDecLatitudeToLL(decimalLatitude);
        longitude = PositioningUtil.convertDecLongitudeToLL(decimalLongitude);
    }
