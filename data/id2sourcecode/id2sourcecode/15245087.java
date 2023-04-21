    @Override
    public void drawAngle(double start, double end, String label) {
        double mid = (start + end) / 2;
        vectorBuffer.append(String.format(ANGLE_FORMAT, start, start, end, mid, label));
    }
