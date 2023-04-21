    public double lineSearch(double probe, RealArray distribution) {
        if (this.size() <= 1) {
            throw new EuclidRuntimeException("unfilled arrays in line search");
        }
        if (this.size() != distribution.size()) {
            throw new EuclidRuntimeException("unequal arrays in line search");
        }
        double[] distArray = distribution.getArray();
        int top = distArray.length - 1;
        int bottom = 0;
        boolean change = true;
        while (change) {
            if (top - bottom <= 1) {
                break;
            }
            change = false;
            int mid = (top + bottom) / 2;
            if (distArray[mid] < probe) {
                bottom = mid;
                change = true;
            } else if (distArray[mid] > probe) {
                top = mid;
                change = true;
            }
        }
        double ratio = (probe - distArray[bottom]) / (distArray[top] - distArray[bottom]);
        double step = array[1] - array[0];
        return this.array[bottom] + step * ratio;
    }
