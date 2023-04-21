    private double getVariant(double[] cache, double[] variant, double value, int lower, int higher) {
        if (lower < higher) {
            while (lower < higher - 1) {
                if (value == cache[lower]) higher = lower + 1; else if (value == cache[higher]) lower = higher - 1; else {
                    int mid = (lower + higher) / 2;
                    if (value < cache[mid]) higher = mid; else lower = mid;
                }
            }
        } else {
            while (higher < lower - 1) {
                if (value == cache[lower]) higher = lower - 1; else if (value == cache[higher]) lower = higher + 1; else {
                    int mid = (lower + higher) / 2;
                    if (value < cache[mid]) higher = mid; else lower = mid;
                }
            }
        }
        if (value == cache[lower]) return variant[lower];
        if (value == cache[higher]) return variant[higher];
        double lower_part = (cache[higher] - value) / (cache[higher] - cache[lower]);
        double position = index2Position(lower) * lower_part + index2Position(higher) * (1.0 - lower_part);
        return cachePosition.getVariant(position);
    }
