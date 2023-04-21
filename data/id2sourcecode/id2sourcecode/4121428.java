    public static int sample(double[] cumulativeProbRatios, Random random) {
        int low = 0;
        int high = cumulativeProbRatios.length - 1;
        double x = random.nextDouble() * cumulativeProbRatios[high];
        while (low < high) {
            int mid = (high + low) / 2;
            if (x > cumulativeProbRatios[mid]) {
                low = mid + 1;
            } else if (high == mid) {
                return (x > cumulativeProbRatios[low]) ? mid : low;
            } else {
                high = mid;
            }
        }
        return low;
    }
