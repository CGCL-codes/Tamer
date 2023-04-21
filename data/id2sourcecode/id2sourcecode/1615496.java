    public double integrate(double a, double b) {
        double lastY, currY, aveY, result = 0;
        int numTraps = 1000;
        VARLIST.setVarVal("x", a);
        lastY = solve();
        double xStep = (b - a) / numTraps;
        int trapCount = 0;
        for (int i = 0; i < numTraps; i++) {
            VARLIST.updateVarVal("x", xStep);
            currY = solve();
            aveY = (lastY + currY) / 2;
            result += aveY * xStep;
            trapCount++;
            lastY = currY;
        }
        return result;
    }
