    public void squareYStep() {
        double yRange = realYMax * (xMax - xMin) / realXMax;
        double y = (yMax + yMin) / 2;
        setYMax(y + yRange / 2);
        setYMin(y - yRange / 2);
    }
