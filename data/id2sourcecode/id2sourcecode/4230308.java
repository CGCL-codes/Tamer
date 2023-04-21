    public Hessian hessianAt(Point point) {
        double[] ax = point.toArray();
        double[][] hessian = new double[ax.length][ax.length];
        switch(functionType) {
            case F1:
                hessian[0][0] = 2 + 26.6479 * Math.cos(3 * Math.PI * ax[0]);
                hessian[0][1] = 0;
                hessian[1][0] = hessian[0][1];
                hessian[1][1] = 4 + 63.1655 * Math.cos(4 * Math.PI * ax[1]);
            case F2:
                hessian[0][0] = 2 + 26.6479 * Math.cos(3 * Math.PI * ax[0]) * Math.cos(4 * Math.PI * ax[1]);
                hessian[0][1] = -35.5306 * Math.sin(3 * Math.PI * ax[0]) * Math.sin(4 * Math.PI * ax[1]);
                hessian[1][0] = hessian[0][1];
                hessian[1][1] = 4 + 47.3741 * Math.cos(3 * Math.PI * ax[0]) * Math.cos(4 * Math.PI * ax[1]);
                break;
            case F3:
                hessian[0][0] = 2 + 26.6479 * Math.cos(Math.PI * (3 * ax[0] + 4 * ax[1]));
                hessian[0][1] = 35.5306 * Math.cos(Math.PI * (3 * ax[0] + 4 * ax[1]));
                hessian[1][0] = hessian[0][1];
                hessian[1][1] = 4 + 47.3741 * Math.cos(Math.PI * (3 * ax[0] + 4 * ax[1]));
        }
        return Hessian.valueOf(hessian);
    }
