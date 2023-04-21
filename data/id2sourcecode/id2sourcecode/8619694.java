    public static double[] executeTriangle(double min, double max, int zecs, double value1, double value2, Double k) {
        if (k == null) {
            k = 1.0;
        }
        double child1 = 0;
        double child2 = 0;
        double g1 = Math.min(value1, value2);
        double g2 = Math.max(value1, value2);
        double vmin1 = 0, mod1 = 0, vmax1 = 0;
        double vmin2 = 0, mod2 = 0, vmax2 = 0;
        double kprec = Math.pow(1, -zecs);
        if (g1 != g2) {
            double d = g2 - g1;
            if ((min < g1) && (g1 < g2)) {
                vmin1 = g1 - k * d;
                if (vmin1 < min) vmin1 = min;
                mod1 = g1;
                vmax1 = g2;
                vmin2 = g1;
                mod2 = g2;
                vmax2 = g2 + k * d;
                if (vmax2 > max) vmax2 = max;
            }
            if ((min == g1) && (g1 < g2)) {
                vmin1 = min;
                mod1 = (g1 + g2) / 2;
                vmax1 = g2;
                vmin2 = min;
                mod2 = (g1 + g2) / 2;
                vmax2 = g2;
            }
            if ((g1 < g2) && (g2 == max)) {
                vmin1 = g1;
                mod1 = (g1 + g2) / 2;
                vmax1 = g2;
                vmin2 = g1;
                mod2 = (g1 + g2) / 2;
                vmax2 = g2;
            }
        } else {
            double d = Math.min(g1 - min, max - g2);
            if ((min < g1) && (g1 == g2) && (g2 < max)) {
                vmin1 = g1 - kprec;
                mod1 = g1;
                vmax1 = g1 + kprec;
                vmin2 = g2 - kprec;
                mod2 = g2;
                vmax2 = g2 + kprec;
            }
            if ((g1 == g2) && (g2 == min)) {
                vmin1 = g1;
                mod1 = g1 + (d / 2);
                vmax1 = g1 + d;
                vmin2 = g2;
                mod2 = g2 + (d / 2);
                vmax2 = g2 + d;
            }
            if ((g1 == g2) && (g2 == max)) {
                vmin1 = g1 - d;
                mod1 = g1 - (d / 2);
                vmax1 = g1;
                vmin2 = g2 - d;
                mod2 = g2 - (d / 2);
                vmax2 = g2;
            }
        }
        if (vmin1 < min) vmin1 = min;
        if (vmin2 < min) vmin2 = min;
        if (vmax1 > max) vmax1 = max;
        if (vmax2 > max) vmax2 = max;
        child1 = triangle(vmin1, mod1, vmax1);
        child2 = triangle(vmin2, mod2, vmax2);
        return new double[] { child1, child2 };
    }
