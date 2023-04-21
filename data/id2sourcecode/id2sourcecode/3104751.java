    public double searchMaximumFromFunctionFromX(double xStart, double incrFactor, Collection<Point2D> helpObject, FunctionFromX functionFromX) {
        double x1, x2, xTest;
        double y1, y2, yTest;
        x1 = x2 = xTest = xStart;
        y1 = y2 = yTest = functionFromX.execute(xTest, helpObject);
        for (int i = 0; i < 1000000; i++) {
            xTest *= incrFactor;
            yTest = functionFromX.execute(xTest, helpObject);
            if (yTest < y1) {
                x1 = xTest;
                y1 = yTest;
                break;
            }
            x2 = x1;
            x1 = xTest;
            y2 = y1;
            y1 = yTest;
        }
        for (int i = 0; i < 1000000; i++) {
            xTest = (x1 + x2) / 2;
            yTest = functionFromX.execute(xTest, helpObject);
            if (y2 >= y1) {
                x1 = xTest;
                y1 = yTest;
            } else {
                x2 = xTest;
                y2 = yTest;
            }
            if (i > 10 && Math.abs(y2 - y1) < 1.0E-12) {
                break;
            }
        }
        return xTest;
    }
