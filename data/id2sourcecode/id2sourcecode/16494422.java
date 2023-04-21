    public void squareXStep() {
        double xRange = realXMax * (yMax - yMin) / realYMax;
        double x = (xMax + xMin) / 2;
        setXMax(x + xRange / 2);
        setXMin(x - xRange / 2);
    }
