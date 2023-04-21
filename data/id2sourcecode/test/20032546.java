    private void transform() throws InkMLComplianceException {
        if (points == null) {
            points = new double[size][getTargetFormat().getChannelCount()];
        }
        getCanvasTransform().transform(sourcePoints, points, getSourceFormat(), getTargetFormat());
        notifyObserver(ON_CHANGE);
    }
