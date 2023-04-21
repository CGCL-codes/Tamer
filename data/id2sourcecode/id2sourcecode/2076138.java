    public double evaluate(double xt) {
        int klo = 0;
        int khi = this._x.length - 1;
        while ((khi - klo) > 1) {
            int k = (khi + klo) / 2;
            if (this._x[k] > xt) khi = k; else klo = k;
        }
        double h;
        double r;
        if ((h = this._x[khi] - this._x[klo]) == 0.0) r = this._y[khi]; else {
            double a;
            double b;
            a = (this._x[khi] - xt) / h;
            b = (xt - this._x[klo]) / h;
            r = a * this._y[klo] + b * this._y[khi] + (((a * a * a) - a) * this._coeff[klo] + ((b * b * b) - b) * this._coeff[khi]) * (h * h) / 6.0;
        }
        return r;
    }
