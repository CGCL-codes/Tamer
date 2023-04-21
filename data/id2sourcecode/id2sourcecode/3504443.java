    private void autoScale(double min, double max) {
        double powmin = 0, powmax = 0, pow = 0, lower = 0, upper = 0;
        int num = 0;
        double realmin = min;
        double realmax = max;
        double thisScaleMin = scaleMin;
        double thisScaleMax = scaleMax;
        double offset = 0;
        boolean rescale = true;
        int rescaleCount = 0;
        while (rescale) {
            double thisMin = min;
            double thisMax = max;
            if (min == 0 && max == 0 && offset == 0) {
                setOrdinate(0., 1., 5);
                return;
            } else if (min == 0 && max == 0) {
                pow = 0;
                min = -1e-2;
                max = 1e-2;
            } else if (min == 0) pow = Math.floor(Math.log(Math.abs(max)) / Math.log(10)); else if (max == 0) pow = Math.floor(Math.log(Math.abs(min)) / Math.log(10)); else {
                powmin = Math.floor(Math.log(Math.abs(min)) / Math.log(10));
                if (beenScaled && !scaleDecrease) powmin = Math.max(powmin, Math.floor(Math.log(Math.abs(thisScaleMin)) / Math.log(10)));
                powmax = Math.floor(Math.log(Math.abs(max)) / Math.log(10));
                if (beenScaled && !scaleDecrease) powmax = Math.max(powmax, Math.floor(Math.log(Math.abs(thisScaleMax)) / Math.log(10)));
                pow = Math.max(powmin, powmax);
            }
            if (min == max) {
                min -= Math.abs(min / 10);
                max += Math.abs(max / 10);
            }
            min = min / Math.pow(10.0, pow);
            max = max / Math.pow(10.0, pow);
            if (min != 0) min -= min * 1e-4;
            if (max != 0) max -= max * 1e-4;
            if (min >= 0) {
                if (min < 1) lower = 0.; else if (min < 2) lower = 1. * Math.pow(10.0, pow); else if (min < 5) lower = 2. * Math.pow(10.0, pow); else lower = 5. * Math.pow(10.0, pow);
            } else {
                if (min >= -1) lower = -1. * Math.pow(10.0, pow); else if (min >= -2) lower = -2. * Math.pow(10.0, pow); else if (min >= -5) lower = -5. * Math.pow(10.0, pow); else lower = -1. * Math.pow(10.0, pow + 1);
            }
            if (beenScaled && !scaleDecrease) lower = Math.min(lower, thisScaleMin);
            if (max > 0) {
                if (max > 5) upper = Math.pow(10.0, pow + 1); else if (max > 2) upper = 5. * Math.pow(10.0, pow); else if (max > 1) upper = 2. * Math.pow(10.0, pow); else upper = Math.pow(10.0, pow);
            } else {
                if (max > -1) upper = 0.; else if (max > -2) upper = -1. * Math.pow(10.0, pow); else if (max > -5) upper = -2. * Math.pow(10.0, pow); else upper = -5. * Math.pow(10.0, pow);
            }
            if (beenScaled && !scaleDecrease) upper = Math.max(upper, thisScaleMax);
            num = (int) Math.round((upper - lower) / Math.pow(10.0, pow));
            if (num < 3) num *= 4; else if (num < 5) num *= 2; else if (num > 10 && num % 2 == 0) num /= 2; else if (num == 15) num = 6;
            boolean doZoom = true;
            if (beenScaled && !scaleDecrease) doZoom = false;
            if (rescaleCount++ >= 2) doZoom = false;
            if ((upper - lower) / (realmax - realmin) < 10) doZoom = false;
            if (doZoom) {
                double ave = (thisMax + thisMin) / 2;
                double[] gridline = new double[num + 1];
                double gridSpace = (upper - lower) / num;
                double delta = Double.MAX_VALUE;
                int gridnum = 0;
                for (int i = 0; i < num + 1; i++) {
                    gridline[i] = lower + i * gridSpace;
                    double thisDelta = Math.abs(ave - gridline[i]);
                    if (thisDelta < delta) {
                        delta = thisDelta;
                        gridnum = i;
                    }
                }
                min = thisMin - gridline[gridnum];
                max = thisMax - gridline[gridnum];
                thisScaleMin -= gridline[gridnum];
                thisScaleMax -= gridline[gridnum];
                offset += gridline[gridnum];
                if (rescaleCount > 2) rescale = false;
            } else {
                rescale = false;
            }
        }
        lower += offset;
        upper += offset;
        beenScaled = true;
        scaleMin = lower;
        scaleMax = upper;
        scaleDiv = num;
        setOrdinate(scaleMin, scaleMax, scaleDiv);
    }
