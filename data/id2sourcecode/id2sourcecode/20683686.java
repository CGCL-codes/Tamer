    public double getMostReliability(int src, int dest, int time, char mode) {
        int[] minAndMax = findMinAndMaxDuration(time);
        double minDuration = minAndMax[0];
        double maxDuration = minAndMax[1];
        Link[][] mtx;
        bestPath.clear();
        while ((int) maxDuration - (int) minDuration > 1) {
            boolean[] flag = new boolean[linkMatrix.length];
            workingPath.clear();
            double lowerBound = (minDuration + maxDuration) / 2;
            mtx = prune(lowerBound, time, mode);
            if (canRoute(src, dest, mtx, time, flag)) {
                minDuration = lowerBound;
                bestPath = new Path(workingPath);
            } else {
                maxDuration = lowerBound;
            }
        }
        return maxDuration;
    }
