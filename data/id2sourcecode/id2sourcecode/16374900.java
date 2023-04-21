    public double lineSearch(DifferentiableObjective o, double[] parameters, double[] direction) {
        double min = 0;
        double minVal = evalueateAt(o, parameters, direction, min);
        if (Double.isNaN(minVal)) throw new RuntimeException("Invalid function value: " + minVal);
        double max = defaultStepSize;
        double maxVal = evalueateAt(o, parameters, direction, max);
        if (Double.isNaN(maxVal)) throw new RuntimeException("Invalid function value: " + maxVal);
        while (maxVal > minVal) {
            max = 2 * max;
            maxVal = evalueateAt(o, parameters, direction, max);
            if (Double.isNaN(maxVal)) throw new RuntimeException("Invalid function value: " + maxVal);
        }
        while (max - min > max * 0.05) {
            double mid = (max + min) / 2;
            double midVal = evalueateAt(o, parameters, direction, mid);
            if (Double.isNaN(midVal)) throw new RuntimeException("Invalid function value: " + midVal);
            if (minVal > maxVal) {
                max = mid;
                maxVal = midVal;
            } else {
                min = mid;
                minVal = midVal;
            }
        }
        StaticUtils.add(parameters, parameters, direction, min);
        defaultStepSize = min * 2;
        return minVal;
    }
