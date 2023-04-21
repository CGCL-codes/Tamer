    public static int bsearchDoubleArray(double[] array, int min, int max, double value) {
        if (value < array[min]) {
            return min - 1;
        } else {
            if (array[max] <= value) {
                return max;
            } else {
                int mid;
                while (min + 1 < max) {
                    mid = (min + max) / 2;
                    if (value < array[mid]) {
                        max = mid;
                    } else {
                        min = mid;
                    }
                }
                return min;
            }
        }
    }
