    private static double getMinimumDistanceBetweenHubSubMangersForHub(StringMatrix matrix, int targetHubId) {
        double minimumDistance = Double.MAX_VALUE;
        int minIndex = -1;
        int maxIndex = -1;
        for (int i = 0; i < matrix.getNumberOfRows(); i++) {
            int currentHub = matrix.convertDoubleToInteger(i, 0);
            if (currentHub == targetHubId) {
                if (minIndex == -1) {
                    minIndex = i;
                }
                maxIndex = i;
            }
        }
        if (minIndex == maxIndex) {
            return -1.0;
        }
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int rowFirstHubSubManager = minIndex + random.nextInt(maxIndex - minIndex);
            int rowSecondHubSubManager = minIndex + random.nextInt(maxIndex - minIndex);
            if (rowFirstHubSubManager == rowSecondHubSubManager) {
                continue;
            }
            Coord coordinateFirstHubSubManager = new CoordImpl(matrix.getDouble(rowFirstHubSubManager, 1), matrix.getDouble(rowFirstHubSubManager, 2));
            Coord coordinateSecondHubSubManager = new CoordImpl(matrix.getDouble(rowSecondHubSubManager, 1), matrix.getDouble(rowSecondHubSubManager, 2));
            if (GeneralLib.getDistance(coordinateFirstHubSubManager, coordinateSecondHubSubManager) < minimumDistance) {
                minimumDistance = GeneralLib.getDistance(coordinateFirstHubSubManager, coordinateSecondHubSubManager);
            }
        }
        while (minimumDistance == Double.MAX_VALUE) {
            return -1.0;
        }
        return minimumDistance;
    }
