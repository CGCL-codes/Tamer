    @Override
    public IDirectPosition center() {
        int n = this.getDimension();
        DirectPosition result = new DirectPosition();
        for (int i = 0; i < n; i++) {
            double theMin = this.lowerCorner.getCoordinate(i);
            double theMax = this.upperCorner.getCoordinate(i);
            double val = theMin + (theMax - theMin) / 2;
            if (!Double.isNaN(val)) {
                result.setCoordinate(i, val);
                if (GM_Envelope.logger.isTraceEnabled()) {
                    GM_Envelope.logger.trace("Center " + i + " " + theMin + " " + theMax + " = " + (theMin + (theMax - theMin) / 2));
                }
            }
        }
        return result;
    }
