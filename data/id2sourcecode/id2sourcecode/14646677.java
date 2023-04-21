    private static double findInverse(double x, int lower, int higher) {
        while (lower < higher - 1) {
            if (x == snv_cache[lower]) higher = lower + 1; else if (x == snv_cache[higher]) lower = higher - 1; else {
                int mid = (lower + higher) / 2;
                if (x < snv_cache[mid]) higher = mid; else lower = mid;
            }
        }
        if (x == snv_cache[lower]) return lower * snv_intervall;
        if (x == snv_cache[higher]) return higher * snv_intervall;
        double lower_part = (snv_cache[higher] - x) / (snv_cache[higher] - snv_cache[lower]);
        return lower * lower_part + higher * (1.0 - lower_part);
    }
