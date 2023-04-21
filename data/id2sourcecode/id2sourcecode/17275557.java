    public double interpolate(double xx) {
        if (xx < this.x[0] || xx > this.x[this.noOfPoints - 1]) {
            System.out.print("x(" + xx + ") is outside the range");
            return Double.NaN;
        }
        if (!this.derivativeCalculated) this.calcDerivative();
        double h = 0.0D, b = 0.0D, a = 0.0D, yy = 0.0D;
        int k = 0;
        int klo = 0;
        int khi = this.noOfPoints - 1;
        while (khi - klo > 1) {
            k = (khi + klo) / 2;
            if (this.x[k] > xx) {
                khi = k;
            } else {
                klo = k;
            }
        }
        h = this.x[khi] - this.x[klo];
        a = (this.x[khi] - xx) / h;
        b = (xx - this.x[klo]) / h;
        yy = a * this.y[klo] + b * this.y[khi] + ((a * a * a - a) * this.d2ydx2[klo] + (b * b * b - b) * this.d2ydx2[khi]) * (h * h) / 6.0;
        return yy;
    }
