    public double value(double x) {
        if (coefficients == null) computeSecondDerivatives();
        int n1 = 0;
        int n2 = points.size() - 1;
        while (n2 - n1 > 1) {
            int n = (n1 + n2) / 2;
            if (points.xValueAt(n) > x) n2 = n; else n1 = n;
        }
        double step = points.xValueAt(n2) - points.xValueAt(n1);
        double a = (points.xValueAt(n2) - x) / step;
        double b = (x - points.xValueAt(n1)) / step;
        return a * points.yValueAt(n1) + b * points.yValueAt(n2) + (a * (a * a - 1) * coefficients[n1] + b * (b * b - 1) * coefficients[n2]) * step * step / 6;
    }
